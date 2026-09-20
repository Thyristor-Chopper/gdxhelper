@file:JvmName("SetUtils")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.ObjectSet;

fun <T> ObjectSet<T>.toArray(): GdxArray<T> {
	val output = GdxArray<T>(false, this.size);
	copyToArray(this, output);
	return output;
}

fun <T> ObjectSet<T>.toArray(output: GdxArray<T>) {
	output.clear();
	copyToArray(this, output);
}

private inline fun <T> copyToArray(objectSet: ObjectSet<T>, output: GdxArray<T>) {
	val iterator = objectSet.iterator();
	while(iterator.hasNext)
		output.add(iterator.next());
}
