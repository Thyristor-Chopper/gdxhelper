package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.ArraySupplier;

import io.potatogun.gdxhelper.util.sortWith;

import java.util.function.Predicate;

/**
 * 배열에 대한 읽기 전용 뷰
 *
 * @property array 뷰를 생성할 배열
 */
class ArrayView<T>(private val array: GdxArray<T>) : View<T> {
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("size")
	override val size: Int by array::size;
	override val isEmpty: Boolean
		get() = (array.size == 0);

	override operator fun get(index: Int): T = array[index];

	override fun contains(element: T): Boolean = array.contains(element, false);

	/**
	 * 지정한 요소가 있는지 검사한다.
	 *
	 * @param element  확인할 요소
	 * @param identity false면 값 비교, true면 참조 비교
	 * @return 존재 여부
	 */
	fun contains(element: T, identity: Boolean): Boolean = array.contains(element, identity);

	override fun sortedWith(comparator: Comparator<T>, supplier: ArraySupplier<Array<T>>?): GdxArray<T> {
		val output = toArray(supplier);
		sortWith<T>(output, comparator);
		return output;
	}

	override fun sortedWith(comparator: Comparator<T>, output: GdxArray<T>) {
		toArray(output);
		sortWith<T>(output, comparator);
	}

	override fun filter(condition: Predicate<T>, supplier: ArraySupplier<Array<T>>?): GdxArray<T> {
		val output = if(supplier != null) GdxArray<T>(array.ordered, array.size, supplier) else GdxArray<T>(array.ordered, array.size);
		filter(condition, output);
		return output;
	}

	override fun filter(condition: Predicate<T>, output: GdxArray<T>) {
		filter(condition, output, false);
	}

	fun filter(condition: Predicate<T>, output: GdxArray<T>, optimize: Boolean) {
		output.clear();
		if(optimize)
			for(i in 0 until array.size) {
				val element = array.items[i];
				if(condition.test(element))
					output.add(element);
			}
		else
			for(i in 0 until array.size) {
				val element = array[i];
				if(condition.test(element))
					output.add(element);
			}
	}

	override fun toArray(supplier: ArraySupplier<Array<T>>?): GdxArray<T> {
		val output = if(supplier != null) GdxArray<T>(array.ordered, array.size, supplier) else GdxArray<T>(array.ordered, array.size);
		addToArray(output, supplier != null);
		return output;
	}

	override fun toArray(output: GdxArray<T>) {
		output.clear();
		addToArray(output, false);
	}

	fun toArray(output: GdxArray<T>, optimize: Boolean) {
		output.clear();
		addToArray(output, optimize);
	}

	// toArray 두 군데에서 공통적으로 쓰는 두 줄밖에 안 되는 코드라 인라인이고 소스 코드상 중복 제거가 목적이다.
	private inline fun addToArray(destination: GdxArray<T>, optimize: Boolean) {
		if(optimize)
			for(i in 0 until array.size)
				destination.add(array.items[i]);
		else
			for(i in 0 until array.size)
				destination.add(array[i]);
	}

	override fun iterator(): Iterator<T> = GdxArray.ArrayIterator<T>(array, false);
}
