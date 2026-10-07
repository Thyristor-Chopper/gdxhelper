@file:JvmName("ArrayPoolUtils")
package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Array as GdxArray;

import java.util.function.Consumer;

/**
 * 배열 풀에서 배열을 원하는 크기만큼 늘려서 이번만 빌려서 서브루틴을 실행하고 뭔 짓을 해도 무조건 끝나면 다시 반환한다. (코틀린 전용)
 *
 * @param capacity 필요한 배열 크기
 * @param callback 실행할 서브루틴
 */
@JvmSynthetic inline fun <T> ArrayPool<T>.use(capacity: Int, callback: (GdxArray<T>) -> Unit) {
	val array = obtain(capacity);
	try {
		callback(array);
	} finally {
		free(array);
	}
}

/**
 * 배열 풀에서 배열을 원하는 크기만큼 늘려서 이번만 빌려서 서브루틴을 실행하고 뭔 짓을 해도 무조건 끝나면 다시 반환한다. (자바 전용)
 *
 * 사용을 권장하지 않는다.
 *
 * @param capacity 필요한 배열 크기
 * @param callback 실행할 서브루틴
 */
@Deprecated(message = "using this function is discouraged due to lambda overhead such as variable capturing; manually obtain & free using the try..finally block", level = DeprecationLevel.WARNING)
@SinceKotlin("9999.9")
fun <T> ArrayPool<T>.use(capacity: Int, callback: Consumer<GdxArray<T>>) {
	use(capacity, callback::accept);
}
