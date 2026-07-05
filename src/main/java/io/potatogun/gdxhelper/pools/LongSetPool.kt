package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.LongSet;
import com.badlogic.gdx.utils.Pool;

/**
 * 64비트 정수(원시형) 집합 풀
 */
class LongSetPool(private val capacity: Int) : Pool<LongSet>() {
	override fun newObject(): LongSet = LongSet(capacity);
}
