@file:JvmName("JavaArrayUtils")
package io.potatogun.gdxhelper.collections;

inline fun <T> Array<T>.isArrayOf(klass: Class<T>): Boolean {
	return this::class.java.componentType === klass;
}
