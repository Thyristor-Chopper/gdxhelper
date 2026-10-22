@file:JvmSynthetic
@file:JvmName("CharMapAlias")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

typealias CharMap<V> = IntMap<V>;

inline fun <V> CharMap<V>.get(key: Char): V? = get(key.code);

inline fun <V> CharMap<V>.get(key: Char, default: V): V = get(key.code, default);

inline fun <V> CharMap<V>.put(key: Char, value: V) = put(key.code, value);

fun <V> ObjectMap<Char, V>.toCharMap(): CharMap<V> {
	val output = CharMap<V>(size);
	val iterator = keys();
	while(iterator.hasNext) {
		val key = iterator.next();
		output.put(key.code, get(key));
	}
	return output;
}
