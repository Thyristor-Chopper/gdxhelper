package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.IdentitySet;
import com.badlogic.gdx.utils.Pool;

/**
 * identity를 쓰는 객체 집합 풀
 */
class IdentitySetPool<T> @JvmOverloads constructor(private val capacity: Int, private val autoClear: Boolean = true) : Pool<IdentitySet<T>>() {
	override fun newObject(): IdentitySet<T> = IdentitySet<T>(capacity);

	override fun reset(set: IdentitySet<T>) {
		if(autoClear)
			set.clear();
	}
}
