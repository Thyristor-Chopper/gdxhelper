@file:JvmName("EntityQuery")
package io.potatogun.gdxhelper.entity.manager;

import com.badlogic.gdx.utils.Array as GdxArray;

import io.potatogun.gdxhelper.entity.Entity;

import java.util.function.Predicate;

import kotlin.random.Random;

/**
 * 개체 중 아무거나 반환한다.
 *
 * @return 개체 (없으면 null)
 */
fun EntityManager.getRandom(): Entity? {
	if(view.isEmpty) return null;
	return view[Random.nextInt(view.size)];
}

/**
 * 지정한 종류의 개체들의 수를 반환한다. (코틀린 전용)
 *
 * @return 개체 수
 */
@JvmSynthetic inline fun <reified T : Entity> EntityManager.countOf(): Int = countOf(T::class.java);

/**
 * 지정한 종류의 개체들의 수를 반환한다. (자바 전용)
 *
 * @param type 개체 종류
 * @return 개체 수
 */
fun <T : Entity> EntityManager.countOf(type: Class<T>): Int {
	var ret = 0;
	for(i in 0 until view.size)
		if(type.isInstance(view[i]))
			ret++;
	return ret;
}

/**
 * 지정한 종류의 개체 중 처음으로 등록된 것을 반환한다. (코틀린 전용)
 *
 * @return 개체 (없으면 null)
 */
@JvmSynthetic inline fun <reified T : Entity> EntityManager.getFirstOf(): T? = getFirstOf(T::class.java);

/**
 * 지정한 종류의 개체 중 처음으로 등록된 것을 반환한다. (자바 전용)
 *
 * @param type 개체 종류
 * @return 개체 (없으면 null)
 */
fun <T : Entity> EntityManager.getFirstOf(type: Class<T>): T? {
	for(i in 0 until view.size) {
		val entity = view[i];
		if(type.isInstance(entity))
			return entity as T;
	}
	return null;
}

/**
 * 지정한 종류의 개체를 아무거나 반환한다. (코틀린 전용)
 *
 * @return 개체 (없으면 null)
 */
@JvmSynthetic inline fun <reified T : Entity> EntityManager.getRandomOf(): T? = getRandomOf(T::class.java);

/**
 * 지정한 종류의 개체를 아무거나 반환한다. (자바 전용)
 *
 * @param type 개체 종류
 * @return 개체 (없으면 null)
 */
fun <T : Entity> EntityManager.getRandomOf(type: Class<T>): T? {
	var count = 0;
	var ret: T? = null;
	for(i in 0 until view.size) {
		val entity = view[i];
		if(type.isInstance(entity) && Random.nextInt(++count) == 0)
			ret = entity as T;
	}
	return ret;
}

/**
 * 지정한 개체로부터 가장 가까운 것을 반환한다.
 *
 * @param entity 기준 개체
 * @return 개체 (없으면 null)
 */
fun EntityManager.getClosest(entity: Entity): Entity? {
	if(view.isEmpty) return null;
	var closest = view[0];
	var minDistance = Float.MAX_VALUE;
	for(i in 0 until view.size) {
		val e = view[i];
		val distance = e.distanceTo(entity);
		if(distance < minDistance) {
			minDistance = distance;
			closest = e;
		}
	}
	return closest;
}

/**
 * 지정한 조건의 개체들 중 지정한 개체로부터 가장 가까운 것을 반환한다. (코틀린 전용)
 *
 * 일반적으로 인라인하는 함수들에 비해 좀 크지만 바이트코드가 커지더라도 람다의 crossinline의 이득을 볼 수 있다.
 *
 * 코드가 일반 getClosest와 거의 같지만 인라인 최적화 + 자바용 오버로딩 떄문에 분리되어 있다.
 *
 * @param entity    기준 개체
 * @param condition 개체의 조건
 * @return 개체 (없으면 null)
 */
@JvmSynthetic inline fun EntityManager.getClosest(entity: Entity, crossinline condition: (Entity) -> Boolean): Entity? {
	if(view.isEmpty) return null;
	var closest: Entity? = null;
	var minDistance = Float.MAX_VALUE;
	for(i in 0 until view.size) {
		val e = view[i];
		if(condition(e)) {
			val distance = e.distanceTo(entity);
			if(distance < minDistance) {
				minDistance = distance;
				closest = e;
			}
		}
	}
	return closest;
}

/**
 * 지정한 조건의 개체들 중 지정한 개체로부터 가장 가까운 것을 반환한다. (자바 전용)
 *
 * @param entity    기준 개체
 * @param condition 개체의 조건
 * @return 개체 (없으면 null)
 */
fun EntityManager.getClosest(entity: Entity, condition: Predicate<Entity>): Entity? {
	return getClosest(entity, condition::test);
}

/**
 * 지정한 종류의 개체들 중 지정한 개체로부터 가장 가까운 것을 반환한다. (코틀린 전용)
 *
 * @param entity 기준 개체
 * @return 개체 (없으면 null)
 */
@JvmSynthetic inline fun <reified T : Entity> EntityManager.getClosestOf(entity: Entity): T? = getClosestOf(entity, T::class.java);

/**
 * 지정한 종류의 개체들 중 지정한 개체로부터 가장 가까운 것을 반환한다. (자바 전용)
 *
 * @param type   개체 종류
 * @param entity 기준 개체
 * @return 개체 (없으면 null)
 */
fun <T : Entity> EntityManager.getClosestOf(entity: Entity, type: Class<T>): T? {
	if(view.isEmpty) return null;
	var closest: T? = null;
	var minDistance = Float.MAX_VALUE;
	for(i in 0 until view.size) {
		val e = view[i];
		if(!type.isInstance(e)) continue;
		val distance = e.distanceTo(entity);
		if(distance < minDistance) {
			minDistance = distance;
			closest = e as T;
		}
	}
	return closest;
}

/**
 * 지정한 종류와 조건의 개체들 중 지정한 개체로부터 가장 가까운 것을 반환한다. (코틀린 전용)
 *
 * 일반적으로 인라인하는 함수들에 비해 좀 크지만 바이트코드가 커지더라도 람다의 crossinline의 이득을 볼 수 있다.
 *
 * 코드가 일반 getClosestOf와 거의 같지만 인라인 최적화 + 자바용 오버로딩 떄문에 분리되어 있다.
 *
 * @param entity    기준 개체
 * @param condition 개체의 조건
 * @return 개체 (없으면 null)
 */
@JvmSynthetic inline fun <reified T : Entity> EntityManager.getClosestOf(entity: Entity, crossinline condition: (T) -> Boolean): T? {
	if(view.isEmpty) return null;
	var closest: T? = null;
	var minDistance = Float.MAX_VALUE;
	for(i in 0 until view.size) {
		val e = view[i];
		if(e is T && condition(e)) {
			val distance = e.distanceTo(entity);
			if(distance < minDistance) {
				minDistance = distance;
				closest = e;
			}
		}
	}
	return closest;
}

/**
 * 지정한 종류와 조건의 개체들 중 지정한 개체로부터 가장 가까운 것을 반환한다. (자바 전용)
 *
 * @param entity    기준 개체
 * @param type      개체 종류
 * @param condition 개체의 조건
 * @return 개체 (없으면 null)
 */
fun <T : Entity> EntityManager.getClosestOf(entity: Entity, type: Class<T>, condition: Predicate<T>): T? {
	if(view.isEmpty) return null;
	var closest: T? = null;
	var minDistance = Float.MAX_VALUE;
	for(i in 0 until view.size) {
		val e = view[i];
		if(type.isInstance(e) && condition.test(e as T)) {
			val distance = e.distanceTo(entity);
			if(distance < minDistance) {
				minDistance = distance;
				closest = e as T;
			}
		}
	}
	return closest;
}

/** 
 * 기준 개체의 거리순으로 정렬할 수 있는 비교기를 반환한다.
 *
 * @param entity 기준 개체
 * @return 비교기
 */
inline fun distanceComparator(entity: Entity): Comparator<Entity> = compareBy { it.distanceTo(entity) };

/**
 * 지정한 개체와 가까운 순으로 정렬된 개체 목록을 반환한다.
 *
 * @param entity 기준 개체
 * @return 개체 목록
 */
inline fun EntityManager.getDistanceSorted(entity: Entity): GdxArray<Entity> = view.sortedWith(distanceComparator(entity));

/**
 * 지정한 개체와 가까운 순으로 정렬된 개체 목록을 반환한다.
 *
 * @param entity 기준 개체
 * @param output 개체 목록
 */
inline fun EntityManager.getDistanceSorted(entity: Entity, output: GdxArray<Entity>) {
	view.sortedWith(distanceComparator(entity), output);
}
