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
 * @property slack           스냅샷 이후 개체가 기준 축으로 이동해 생기는 조회 누락을 줄이는 여유분
 */
class SweepPruneManager @JvmOverloads constructor(capacity: Int, nearbyThreshold: Float, private val axis: Axis = Axis.X, private val slack: Float = 0f) : LinearManager(capacity, nearbyThreshold, true) {
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
				val entity = allEntities[i];
				val y = entity.y;
				var j = i - 1;
				while(j >= 0 && allEntities[j].y > y) {
					allEntities[j + 1] = allEntities[j];
					j--;
				}
				allEntities[j + 1] = entity;
			}

			for(i in 0 until size)
				keys[i] = allEntities[i].y;
		} else {
			for(i in 1 until size) {
				val entity = allEntities[i];
				val x = entity.x;
				// 빌어먹을 코틀린 왜 for(초기식; 조건식; 증감식) 문법 없어 확 그냥
				var j = i - 1;
				while(j >= 0 && allEntities[j].x > x) {
					allEntities[j + 1] = allEntities[j];
					j--;
				}
				allEntities[j + 1] = entity;
			}

			for(i in 0 until size)
				keys[i] = allEntities[i].x;
		}
	}

	override fun getNearby(entity: Entity, output: GdxArray<Entity>) {
		output.clear();

		val x = entity.x;
		val y = entity.y;
		val range = nearbyThreshold + slack;
		val base = if(axis == Axis.Y) y else x;
		val maxKey = base + range;
		val size = allEntities.size;

		var i = lowerBound(base - range, size);
		while(i < size && keys[i] <= maxKey) {
			val e = allEntities[i];
			if(e !== entity && abs(e.x - x) <= nearbyThreshold && abs(e.y - y) <= nearbyThreshold && e.distanceTo(entity) <= nearbyThreshold)
				output.add(e);
			i++;
		}
	}

	private inline fun lowerBound(key: Float, size: Int): Int {  // getNearby에서 한 번만 쓰이므로 인라인이다.
		var lo = 0;
		var hi = size;
		while(lo < hi) {
			val mid = (lo + hi) ushr 1;
			if(keys[mid] < key)
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
