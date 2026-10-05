@file:JvmName("PoolUtils")
package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Pool;

import java.util.function.Consumer;

/**
 * 풀에서 객체를 이번만 빌려서 서브루틴을 실행하고 뭔 짓을 해도 무조건 끝나면 다시 반환한다. (코틀린 전용)
 *
 * @param callback 실행할 서브루틴
 */
@JvmSynthetic inline fun <T> Pool<T>.use(callback: (T) -> Unit) {
	val item = obtain();
	try {
		callback(item);
	} finally {
		free(item);
	}
}

/**
 * 풀에서 객체를 이번만 빌려서 서브루틴을 실행하고 뭔 짓을 해도 무조건 끝나면 다시 반환한다. (자바 전용)
 *
 * 사용을 권장하지 않는다.
 *
 * @param callback 실행할 서브루틴
 */
@Deprecated(message = "using this function is discouraged due to lambda overhead such as variable capturing; manually obtain & free using the try..finally block", level = DeprecationLevel.WARNING)
@SinceKotlin("9999.9")
fun <T> Pool<T>.use(callback: Consumer<T>) {
	use(callback::accept);
}
