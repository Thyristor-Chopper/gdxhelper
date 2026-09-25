package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Pool;

/**
 * 수정 가능한 리스트 풀
 */
class MutableListPool<T>(private val autoClear: Boolean = true) : Pool<MutableList<T>>() {
	override fun newObject(): MutableList<T> = mutableListOf<T>();

	override fun reset(list: MutableList<T>) {
		if(autoClear)
			list.clear();
	}
}
