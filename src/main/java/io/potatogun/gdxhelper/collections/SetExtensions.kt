@file:JvmName("SetUtils")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.ArraySupplier;
import com.badlogic.gdx.utils.ObjectSet;

@JvmOverloads fun <T> ObjectSet<T>.toArray(supplier: ArraySupplier<Array<T>>? = null): GdxArray<T> {
	val output = if(supplier != null) GdxArray<T>(false, this.size, supplier) else GdxArray<T>(false, this.size);
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
