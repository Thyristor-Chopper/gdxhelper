@file:JvmName("PoolUtils")
package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Pool;

/**
 * 풀에서 객체를 이번만 빌려서 서브루틴을 실행하고 뭔 짓을 해도 무조건 끝나면 다시 반환한다.
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
