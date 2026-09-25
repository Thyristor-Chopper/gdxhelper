@file:JvmName("EntityUtils")
package io.potatogun.gdxhelper.entity;

import io.potatogun.gdxhelper.util.nextFloat;

import kotlin.random.Random;

/**
 * 임의의 각도로 회전한다.
 */
inline fun Entity.rotateToRandom() {
	rotate(Random.nextFloat(360f));
}
