package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.ArraySupplier;
import com.badlogic.gdx.utils.Pool;

import io.potatogun.gdxhelper.util.ArraySuppliers;

/**
 * 순서 없는 배열 풀
 */
class UnorderedArrayPool<T> @JvmOverloads constructor(private val capacity: Int, private val autoClear: Boolean = true, private val supplier: ArraySupplier<Array<T>>? = null) : Pool<GdxArray<T>>() {
	override fun newObject(): GdxArray<T> = if(supplier != null) GdxArray<T>(false, capacity, supplier) else GdxArray<T>(false, capacity);

	override fun reset(array: GdxArray<T>) {
		if(autoClear)
			array.clear();
	}
}
