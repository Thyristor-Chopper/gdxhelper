@file:JvmName("MapUtils")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * 맵의 지정한 키의 값이 지정한 조건을 만족하지 않으면 지운다. (값이 null이면 처리하지 않는다.)
 *
 * @param key       키
 * @param condition 조건
 * @return 조건 만족 여부
 */
@JvmSynthetic inline fun <K, V> ObjectMap<K, V>.ensureValue(key: K, crossinline condition: (V) -> Boolean): Boolean {
	val value: V? = get(key);
	if(value != null && !condition(value)) {
		remove(key);
		return false;
	}
	return true;
}
