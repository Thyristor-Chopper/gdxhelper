@file:JvmSynthetic
@file:JvmName("CharMapAlias")
package io.potatogun.gdxhelper.collections;

import com.badlogic.gdx.utils.IntMap;

typealias CharMap<V> = IntMap<V>;

inline fun <V> CharMap<V>.get(key: Char): V? = get(key.code);

inline fun <V> CharMap<V>.get(key: Char, default: V): V = get(key.code, default);

inline fun <V> CharMap<V>.put(key: Char, value: V) = put(key.code, value);
