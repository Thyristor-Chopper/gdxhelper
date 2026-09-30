@file:JvmName("ArrayUtils")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.ArraySupplier;

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
 * @param supplier 내장 배열 생성기 (경고: 반드시 원래 배열과 동일해야 한다.)
 * @return 복사된 배열
 */
@JvmOverloads fun <T> GdxArray<T>.clone(supplier: ArraySupplier<Array<T>>? = null): GdxArray<T> {
	val output = if(supplier != null) GdxArray<T>(ordered, size, supplier) else GdxArray<T>(ordered, size);
	for(i in 0 until size)
		output.add(this.items[i]);
	return output;
}

/**
 * 배열의 모든 원소를 지정한 배열로 복사한다.(각 원소는 얕은 복사)
 *
 * @param output   대상 배열
 * @param optimize 최적화 여부 (경고: 원 배열에 ArraySupplier를 사용했고 그 자료형이 대상 배열과 동일한 경우에만 써야 한다.)
 */
@JvmOverloads fun <T> GdxArray<T>.clone(output: GdxArray<T>, optimize: Boolean = false) {
	output.clear();
	if(optimize)
		for(i in 0 until size)
			output.add(this.items[i]);
	else
		for(i in 0 until size)
			output.add(this[i]);
}
