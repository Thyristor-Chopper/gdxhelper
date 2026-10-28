package io.potatogun.gdxhelper.entity.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array as GdxArray;

import io.potatogun.gdxhelper.Window;
import io.potatogun.gdxhelper.collections.View;
import io.potatogun.gdxhelper.collections.createView;
import io.potatogun.gdxhelper.entity.Entity;
import io.potatogun.gdxhelper.util.ArraySuppliers;
import io.potatogun.gdxhelper.util.max2;
import io.potatogun.gdxhelper.world.Freezable;
import io.potatogun.gdxhelper.world.World;

/**
 * 배열을 내부적으로 사용하는 개체 관리자
 *
 * @param capacity 개체 배열 초기 크기
 * @param ordered  개체 등록 순서 보장 여부
 */
abstract class ArrayEntityManager @JvmOverloads constructor(capacity: Int, ordered: Boolean = false) : EntityManager {
	/**
	 * 모든 개체 목록을 담는 배열
	 */
	@JvmField protected val allEntities = GdxArray<Entity>(ordered, capacity, ArraySuppliers.entity);
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("view")
	override val view: View<Entity> = allEntities.createView();
	private lateinit var world: World;  // 한 번 할당된 후에는 바뀌지 않음

	override fun draw(batch: SpriteBatch) {
		if(allEntities.size == 0) return;
		if(!initWorld()) return;

		val halfScreenWidth = Window.width * 0.5f;
		val halfScreenHeight = Window.height * 0.5f;

		val world = this.world;
		val offsetX = world.cameraX;
		val offsetY = world.cameraY;

		for(i in 0 until allEntities.size) {
			val entity = allEntities.items[i];
			// 보이는 개체만 그리기 (자원 낭비 감소)
			val maxLength = max2(entity.width, entity.height);
			val entityX = entity.x;
			val entityY = entity.y;
			if(entityX >= offsetX - halfScreenWidth - maxLength && entityX <= offsetX + halfScreenWidth + maxLength && entityY >= offsetY - halfScreenHeight - maxLength && entityY <= offsetY + halfScreenHeight + maxLength)
				entity.draw(batch);
		}
	}

	override fun update(delta: Float) {
		commit();

		if(allEntities.size > 0 && initWorld()) {
			val world = this.world;
			val isFrozen = (world is Freezable && world.isFrozen);

			for(i in 0 until allEntities.size) {
				val entity = allEntities.items[i];
				if(!isFrozen || entity.isUpdatableWhileFrozen)
					entity.update(delta);
				entity.forceUpdate(delta);
			}
		}

		commit();
	}

	private fun initWorld(): Boolean {
		if(::world.isInitialized) return true;

		for(i in 0 until allEntities.size) {
			val entity = allEntities.items[i];
			val world = entity.world;
			if(world != null) {
				this.world = world;
				return true;
			}
		}
		return false;
	}

	override fun dispose() {
		for(i in 0 until allEntities.size)
			allEntities.items[i].dispose();
	}
}
