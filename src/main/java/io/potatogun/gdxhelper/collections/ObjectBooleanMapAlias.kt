@file:JvmSynthetic
@file:JvmName("ObjectBooleanMapAlias")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.ObjectIntMap;

typealias ObjectBooleanMap<K> = ObjectIntMap<K>;

inline fun <K> ObjectBooleanMap<K>.get(key: K): Boolean = (get(key, 0) != 0);

inline fun <K> ObjectBooleanMap<K>.get(key: K, default: Boolean): Boolean = (get(key, if(default) 1 else 0) != 0);

inline fun <K> ObjectBooleanMap<K>.put(key: K, value: Boolean) {
	put(key, if(value) 1 else 0);
}

inline fun <K> ObjectBooleanMap<K>.put(key: K, value: Boolean, default: Boolean): Boolean {
	val previous = get(key, default);
	put(key, if(value) 1 else 0);
	return previous;
}

fun <K> ObjectIntMap<K>.toObjectBooleanMap(): ObjectBooleanMap<K> {
	val output = ObjectBooleanMap<K>(size);
	val iterator = keys();
	while(iterator.hasNext) {
		val key = iterator.next();
		output.put(key, if(get(key, 0) != 0) 1 else 0);
	}
	return output;
}
