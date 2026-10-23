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
 * @return 복사된 배열
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
 * @param output   대상 배열
 */
fun <T> GdxArray<T>.clone(output: GdxArray<T>) {
	output.clear();
	output.ensureFullCapacity(size);
	for(i in 0 until size)
		output.add(this[i]);
}

/**
 * 배열의 크기가 지정한 크기보다 작으면 확장한다.
 *
 * 기존 ensureCapacity는 추가로 필요한 용량을 받는데 이 확장함수는 필요한 전체 크기로 판단한다.
 *
 * @param capacity 요구 용량
 */
inline fun <T> GdxArray<T>.ensureFullCapacity(capacity: Int) {
	if(capacity > items.size)  // backing array의 크기가 원하는 크기보다 작을 때만 실행. 디컴파일해서 확인한 결과 getter 메쏘드가 아닌 jvm arraylength 바이트코드를 쓰는 .length 접근으로 바뀐다.
		ensureCapacity(capacity - size);  // ensureCapacity는 backing array의 크기가 아닌 현재 항목의 크기 기준으로 동작.
}
