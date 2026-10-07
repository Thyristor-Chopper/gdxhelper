package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.ArraySupplier;
import com.badlogic.gdx.utils.Pool;

import io.potatogun.gdxhelper.util.ArraySuppliers;

/**
 * 배열 풀
 */
open class ArrayPool<T> @JvmOverloads constructor(private val capacity: Int, private val ordered: Boolean = true, private val autoClear: Boolean = true, private val supplier: ArraySupplier<Array<T>>? = null) : Pool<GdxArray<T>>() {
	override fun newObject(): GdxArray<T> = if(supplier != null) GdxArray<T>(ordered, capacity, supplier) else GdxArray<T>(ordered, capacity);

	/**
	 * 배열을 지정한 크기를 보장해서 렌탈한다.
	 *
	 * 간단한 함수고 원래는 직접 호출처에서 작성했을 코드라서 인라인이다.
	 *
	 * @param capacity 필요한 배열 크기
	 */
	inline fun obtain(capacity: Int): GdxArray<T> {
		val array = obtain();
		val length = array.items.size;
		if(length < capacity)
			array.ensureCapacity(capacity - length);
		return array;
	}

	override fun reset(array: GdxArray<T>) {
		if(autoClear)
			array.clear();
	}
}
