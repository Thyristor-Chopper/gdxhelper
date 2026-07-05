package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;

/**
 * 객체 집합 풀
 */
class ObjectSetPool<T> @JvmOverloads constructor(private val capacity: Int, private val autoClear: Boolean = true) : Pool<ObjectSet<T>>() {
	override fun newObject(): ObjectSet<T> = ObjectSet<T>(capacity);

	override fun reset(obj: ObjectSet<T>) {
		if(autoClear) obj.clear();
	}
}
