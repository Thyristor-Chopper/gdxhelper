@file:JvmName("RandomExtensions")
package io.potatogun.gdxhelper.util;

import kotlin.random.Random;

/**
 * 지정한 범위에서 무작위 4바이트 부동소수점 실수를 생성한다.
 *
 * @param min 시작 범위
 * @param max 끝 범위
 */
inline fun Random.nextFloat(min: Float, max: Float): Float = min + (nextFloat() * (max - min));

/**
 * 무작위 부호(-1 또는 1)을 생성한다.
 */
inline fun Random.nextSign(): Int = if(nextBoolean()) 1 else -1;
