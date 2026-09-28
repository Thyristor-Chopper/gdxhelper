@file:JvmName("ArrayUtils")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.Array as GdxArray;

import kotlin.random.Random;

inline fun <T> GdxArray<T>.randomOrNull(): T? = if(size == 0) null else this[Random.nextInt(size)];

/**
 * 배열에 대한 읽기 전용 뷰를 생성한다.
 *
 * 자바에서는 ArrayUtils.createView(array)로 하면 된다.
 *
 * @return 배열 뷰
 */
inline fun <T> GdxArray<T>.createView(): ArrayView<T> = ArrayView<T>(this);

/**
 * 배열의 모든 원소를 지정한 배열로 복사한다.(각 원소는 얕은 복사)
 *
 * @param output 대상 배열
 */
fun <T> GdxArray<T>.clone(): GdxArray<T> {
	val output = GdxArray<T>(ordered, size);
	for(i in 0 until size)
		output.add(this[i]);
	return output;
}

/**
 * 배열의 모든 원소를 지정한 배열로 복사한다.(각 원소는 얕은 복사)
 *
 * @param output 대상 배열
 */
fun <T> GdxArray<T>.clone(output: GdxArray<T>) {
	output.clear();
	for(i in 0 until size)
		output.add(this[i]);
}
