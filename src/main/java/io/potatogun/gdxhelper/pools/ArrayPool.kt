package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.Pool;

/**
 * 배열 풀
 */
class ArrayPool<T> @JvmOverloads constructor(private val capacity: Int, private val autoClear: Boolean = true) : Pool<GdxArray<T>>() {
	override fun newObject(): GdxArray<T> = GdxArray<T>(false, capacity);

	override fun reset(obj: GdxArray<T>) {
		if(autoClear) obj.clear();
	}
}
