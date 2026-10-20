package io.potatogun.gdxhelper.entity.manager;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.FloatArray;

import io.potatogun.gdxhelper.entity.Entity;
import io.potatogun.gdxhelper.util.abs;

/**
 * 축 정렬 목록을 유지하는 Sweep & Prune 방식 개체 관리자
 *
 * @param    capacity        처음 개체 목록 크기
 * @param    nearbyThreshold getNearby에서 사용할 가깝다의 기준
 * @property axis            정렬 기준 축
 */
class SweepPruneManager @JvmOverloads constructor(capacity: Int, nearbyThreshold: Float, private val axis: Axis = Axis.X) : LinearManager(capacity, nearbyThreshold, true) {
	private val keys = FloatArray(capacity);

	override fun update(delta: Float) {
		super.update(delta);

		sort();
	}

	private inline fun sort() {  // update에서만 한 번 쓰이므로 인라인
		val size = allEntities.size;
		if(keys.size != size)
			keys.setSize(size);

		// when 문 쓰면 쓸 데 없이 WhenMappings가 생겨서
		if(axis == Axis.Y) {
			for(i in 1 until size) {
				val entity = allEntities.items[i];
				val y = entity.y;
				var j = i - 1;
				while(j >= 0 && allEntities.items[j].y > y) {
					allEntities.items[j + 1] = allEntities.items[j];
					j--;
				}
				allEntities.items[j + 1] = entity;
			}

			for(i in 0 until size)
				keys.items[i] = allEntities.items[i].y;
		} else {
			for(i in 1 until size) {
				val entity = allEntities.items[i];
				val x = entity.x;
				// 빌어먹을 코틀린 왜 for(초기식; 조건식; 증감식) 문법 없어 확 그냥
				var j = i - 1;
				while(j >= 0 && allEntities.items[j].x > x) {
					allEntities.items[j + 1] = allEntities.items[j];
					j--;
				}
				allEntities.items[j + 1] = entity;
			}

			for(i in 0 until size)
				keys.items[i] = allEntities.items[i].x;
		}
	}

	override fun getNearby(base: Entity, output: GdxArray<Entity>) {
		output.clear();

		val x = base.x;
		val y = base.y;
		val baseKey = if(axis == Axis.Y) y else x;
		val maxKey = baseKey + nearbyThreshold;
		val size = allEntities.size;

		var i = lowerBound(baseKey - nearbyThreshold, size);
		while(i < size && keys.items[i] <= maxKey) {
			val entity = allEntities.items[i];
			if(entity !== base && abs(entity.x - x) <= nearbyThreshold && abs(entity.y - y) <= nearbyThreshold && entity.distanceTo(base) <= nearbyThreshold)
				output.add(entity);
			i++;
		}
	}

	private inline fun lowerBound(key: Float, size: Int): Int {  // getNearby에서 한 번만 쓰이므로 인라인이다.
		var lo = 0;
		var hi = size;
		while(lo < hi) {
			val mid = (lo + hi) ushr 1;
			if(keys.items[mid] < key)
				lo = mid + 1;
			else
				hi = mid;
		}
		return lo;
	}

	enum class Axis {
		X,
		Y;
	}
}
