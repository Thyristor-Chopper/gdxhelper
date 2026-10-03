@file:JvmName("EntityUtils")
package io.potatogun.gdxhelper.entity;

import io.potatogun.gdxhelper.util.nextFloat;
import io.potatogun.gdxhelper.world.World;

import kotlin.random.Random;

/**
 * 임의의 각도로 회전한다.
 */
inline fun Entity.rotateToRandom() {
	rotate(Random.nextFloat(360f));
}

/**
 * 개체가 지정한 월드에 있는지 확인한다.
 *
 * @param world 확인할 월드
 */
inline fun Entity.isIn(world: World?): Boolean = world != null && this.world === world;
