package io.potatogun.gdxhelper.entity.manager;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.Pool;

import io.potatogun.gdxhelper.collections.clone;
import io.potatogun.gdxhelper.entity.Entity;
import io.potatogun.gdxhelper.pools.ArrayPool;
import io.potatogun.gdxhelper.pools.UnorderedArrayPool;
import io.potatogun.gdxhelper.util.max2;
import io.potatogun.gdxhelper.util.min2;

/**
 * 쿼드 트리 기반 개체 관리자
 *
 * 일반 쿼드트리보다는 Loose Quadtree에 가까운 구현이다.
 *
 * @param    capacity        처음 개체 목록 크기
 * @param    width           월드의 너비
 * @param    height          월드의 높이
 * @property nearbyThreshold getNearby에서 사용할 가깝다의 기준
 * @property maxNodeEntities 각 트리 노드당 최대 개체 수
 * @property maxDepth        트리의 최대 깊이
 */
class QuadTree(capacity: Int, width: Float, height: Float, private val nearbyThreshold: Float, private val maxNodeEntities: Int, private val maxDepth: Int) : ArrayEntityManager(capacity) {
	private val addQueue = GdxArray<Entity>(false, 8);
	private val removeQueue = GdxArray<Entity>(false, 8);
	private val entityNodeMap = IdentityMap<Entity, Node>(capacity);
	private val entityArrayPool = UnorderedArrayPool<Entity>(maxNodeEntities);
	private val childrenPool = ArrayPool<Node>(4);
	private val nodePool = NodePool();
	private val root: Node;

	init {
		if(width <= 0f)
			throw IllegalArgumentException("invalid width");
		if(height <= 0f)
			throw IllegalArgumentException("invalid height");
		if(maxNodeEntities <= 0)
			throw IllegalArgumentException("invalid maximum entities per node");
		if(maxDepth < 0)
			throw IllegalArgumentException("invalid maximum depth");

		root = nodePool.obtain(0f, 0f, width, height, 0, null);
	}

	override fun add(entity: Entity): Boolean {
		if(entity.isDisposed)
			throw IllegalStateException("entity is disposed");
		if(entity.world.entities !== this)
			throw IllegalStateException("entity belongs to a different world");
		if(removeQueue.contains(entity, true)) {
			removeQueue.removeValue(entity, true);
			return true;
		}
		if(entityNodeMap.containsKey(entity) || addQueue.contains(entity, true))
			return false;
		addQueue.add(entity);
		return true;
	}

	override fun remove(entity: Entity): Boolean {
		if(addQueue.contains(entity, true)) {
			addQueue.removeValue(entity, true);
			return true;
		}
		if(!entityNodeMap.containsKey(entity) || removeQueue.contains(entity, true))
			return false;
		removeQueue.add(entity);
		return true;
	}

	override fun update(delta: Float) {
		super.update(delta);

		if(removeQueue.size > 0) {
			for(i in 0 until removeQueue.size) {
				val entity = removeQueue[i];
				entityNodeMap.remove(entity)?.let { node ->
					node.entities.removeValue(entity, true);
					node.merge();
				};
				allEntities.removeValue(entity, true);
			}
			removeQueue.clear();
		}

		if(addQueue.size > 0) {
			for(i in 0 until addQueue.size) {
				val entity = addQueue[i];
				root.insert(entity);
				allEntities.add(entity);
			}
			addQueue.clear();
		}
	}

	override fun updatePosition(entity: Entity) {
		val node = entityNodeMap[entity] ?: return;
		if(node.contains(entity)) {
			node.pushDown(entity);
			return;
		} else if(node.parent == null) {
			return;
		}

		node.entities.removeValue(entity, true);
		var target: Node? = node.parent;
		while(target != null && !target.contains(entity))
			target = target.parent;
		if(target == null)
			target = root;
		target.insert(entity);
		if(node.children != null || target !== node.parent)
			node.merge(target);
	}

	override fun getNearby(entity: Entity): GdxArray<Entity> {
		val output = GdxArray<Entity>(false, allEntities.size);
		getNearby(entity, output);
		return output;
	}

	override fun getNearby(entity: Entity, output: GdxArray<Entity>) {
		output.clear();

		val halfLength = max2(entity.width, entity.height) * 0.5f + nearbyThreshold;
		val qx0 = entity.x - halfLength;
		val qy0 = entity.y - halfLength;
		val qx1 = entity.x + halfLength;
		val qy1 = entity.y + halfLength;

		root.query(entity, qx0, qy0, qx1, qy1, output);
	}

	override fun dispose() {
		super.dispose();

		// Entity#setWorld를 했는데 새 월드를 한 번도 연 적이 없어서 update가 되지 않아 실제로는 추가되지 않고 고립되는 상황 방지
		if(addQueue.size > 0)
			for(i in 0 until addQueue.size) {
				val entity = addQueue[i];
				entity.dispose();
			}
	}

	/**
	 * 쿼드트리의 노드
	 *
	 * private 클래스이고 QuadTree 내에서만 사용되므로 GC 감소와 성능을 위해 캡슐화나 많은 안전장치가 생략되어 있다.
	 */
	private inner class Node {
		@JvmField var x = 0f;
		@JvmField var y = 0f;
		@JvmField var width = 0f;
		@JvmField var height = 0f;
		@JvmField var depth = 0;
		@JvmField var parent: Node? = null;
		@JvmField val entities = GdxArray<Entity>(false, maxNodeEntities);
		@JvmField var children: GdxArray<Node>? = null;
		@JvmField var midX = 0f;
		@JvmField var midY = 0f;
		@JvmField var minSide = 0f;
		@JvmField var looseX0 = 0f;
		@JvmField var looseY0 = 0f;
		@JvmField var looseX1 = 0f;
		@JvmField var looseY1 = 0f;

		fun insert(entity: Entity) {
			val children = this.children;
			if(children != null) {
				getFittingChild(children, entity)?.let { child ->
					child.insert(entity);
					return;
				};
			}

			entities.add(entity);
			entityNodeMap.put(entity, this);

			if(children == null && entities.size > maxNodeEntities && depth < maxDepth)
				subdivide();
		}

		fun contains(entity: Entity): Boolean = entity.x >= x && entity.x < x + width && entity.y >= y && entity.y < y + height && (depth == 0 || max2(entity.width, entity.height) <= minSide);

		fun subdivide() {
			val halfWidth = width * 0.5f;
			val halfHeight = height * 0.5f;
			val childDepth = depth + 1;

			val node1 = nodePool.obtain(x, y, halfWidth, halfHeight, childDepth, this);
			val node2 = nodePool.obtain(x + halfWidth, y, halfWidth, halfHeight, childDepth, this);
			val node3 = nodePool.obtain(x, y + halfHeight, halfWidth, halfHeight, childDepth, this);
			val node4 = nodePool.obtain(x + halfWidth, y + halfHeight, halfWidth, halfHeight, childDepth, this);
			if(children == null)
				children = childrenPool.obtain();
			else
				children!!.clear();
			children!!.add(node1, node2, node3, node4);

			val existing = entityArrayPool.obtain();
			entities.clone(existing);
			entities.clear();
			for(i in 0 until existing.size) {
				val entity = existing[i];
				val child = getFittingChild(children!!, entity);
				if(child != null) {
					child.insert(entity);
				} else {
					entities.add(entity);
					entityNodeMap.put(entity, this);
				}
			}
			entityArrayPool.free(existing);
		}

		fun getFittingChild(children: GdxArray<Node>, entity: Entity): Node? {
			val index = (if(entity.x >= midX) 1 else 0) or (if(entity.y >= midY) 2 else 0);
			val child = children[index];
			return if(child.contains(entity)) child else null;
		}

		fun pushDown(entity: Entity) {
			val children = this.children ?: return;
			val child = getFittingChild(children, entity) ?: return;
			entities.removeValue(entity, true);
			child.insert(entity);
		}

		fun merge(stopNode: Node? = null) {
			var node: Node? = this;
			while(node != null && node !== stopNode) {
				val children = node.children;
				if(children != null) {
					var total = node.entities.size;
					for(i in 0 until children.size) {
						val child = children[i];
						if(child.children != null) return;
						total += child.entities.size;
					}
					if(total > maxNodeEntities / 2) return;

					for(i in 0 until children.size) {
						val child = children[i];
						for(j in 0 until child.entities.size) {
							val entity = child.entities[j];
							node.entities.add(entity);
							entityNodeMap.put(entity, node);
						}
						nodePool.free(child);
					}
					childrenPool.free(children);
					node.children = null;
				}
				node = node.parent;
			}
		}

		fun query(entity: Entity, qx0: Float, qy0: Float, qx1: Float, qy1: Float, output: GdxArray<Entity>) {
			if(depth > 0 && (qx1 < looseX0 || qx0 > looseX1 || qy1 < looseY0 || qy0 > looseY1)) return;

			for(i in 0 until entities.size) {
				val e = entities[i];
				if(e !== entity && e.distanceTo(entity) <= nearbyThreshold)
					output.add(e);
			}

			children?.let {
				for(i in 0 until it.size) {
					val child = it[i];
					child.query(entity, qx0, qy0, qx1, qy1, output);
				}
			};
		}
	}

	private inner class NodePool : Pool<Node>() {
		override fun newObject() = Node();

		inline fun obtain(nodeX: Float, nodeY: Float, nodeWidth: Float, nodeHeight: Float, nodeDepth: Int, nodeParent: Node?): Node = obtain().apply {
			x = nodeX;
			y = nodeY;
			width = nodeWidth;
			height = nodeHeight;
			depth = nodeDepth;
			parent = nodeParent;
			midX = x + width * 0.5f;
			midY = y + height * 0.5f;
			minSide = min2(width, height);
			looseX0 = x - width * 0.5f;
			looseY0 = y - height * 0.5f;
			looseX1 = x + width * 1.5f;
			looseY1 = y + height * 1.5f;
		};

		override fun reset(node: Node) {
			node.entities.clear();
			node.children?.let { childrenPool.free(it) };
			node.children = null;
			node.parent = null;
		}
	}
}
