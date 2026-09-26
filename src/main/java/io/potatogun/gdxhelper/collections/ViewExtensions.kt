@file:JvmName("ViewExtensions")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.Array as GdxArray;

/**
 * 인라인으로 람다 오버헤드가 없는 filter이다. 자바에서는 사용할 수 없다.
 *
 * @param output    결과를 저장할 목록 (이미 다른 원소가 있다면 덮어씌워짐)
 * @param condition 조건
 */
@JvmSynthetic inline fun <T> View<T>.filter(output: GdxArray<T>, condition: (T) -> Boolean) {
	output.clear();
	for(i in 0 until size) {
		val element = this[i];
		if(condition(element))
			output.add(element);
	}
}
