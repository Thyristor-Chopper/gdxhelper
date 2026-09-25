package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.Array as GdxArray;

import io.potatogun.gdxhelper.util.Utils;

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

	override fun sortedWith(comparator: Comparator<T>): GdxArray<T> {
		val output = toArray();
		Utils.sortWith<T>(output, comparator);
		return output;
	}

	override fun sortedWith(comparator: Comparator<T>, output: GdxArray<T>) {
		toArray(output);
		Utils.sortWith<T>(output, comparator);
	}

	override fun filter(condition: Predicate<T>): GdxArray<T> {
		val output = GdxArray<T>(array.ordered, array.size);
		filter(condition, output);
		return output;
	}

	override fun filter(condition: Predicate<T>, output: GdxArray<T>) {
		output.clear();
		for(i in 0 until array.size) {
			val element = array[i];
			if(condition.test(element))
				output.add(element);
		}
	}

	override fun toArray(): GdxArray<T> {
		val output = GdxArray<T>(array.ordered, array.size);
		addToArray(output);
		return output;
	}

	override fun toArray(output: GdxArray<T>) {
		output.clear();
		addToArray(output);
	}

	// toArray 두 군데에서 공통적으로 쓰는 두 줄밖에 안 되는 코드라 인라인이고 소스 코드상 중복 제거가 목적이다.
	private inline fun addToArray(destination: GdxArray<T>) {
		for(i in 0 until array.size)
			destination.add(array[i]);
	}

	override fun iterator(): Iterator<T> = GdxArray.ArrayIterator<T>(array, false);
}
