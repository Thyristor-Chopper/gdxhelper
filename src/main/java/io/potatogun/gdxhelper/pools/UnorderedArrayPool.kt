package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.Pool;

/**
 * 순서 없는 배열 풀
 */
class UnorderedArrayPool<T> @JvmOverloads constructor(private val capacity: Int, private val autoClear: Boolean = true) : Pool<GdxArray<T>>() {
	override fun newObject(): GdxArray<T> = GdxArray<T>(false, capacity);

	override fun reset(array: GdxArray<T>) {
		if(autoClear)
			array.clear();
	}
}
