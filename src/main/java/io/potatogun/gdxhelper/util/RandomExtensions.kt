@file:JvmName("RandomExtensions")
package io.potatogun.gdxhelper.util;

import kotlin.random.Random;

inline fun Random.nextFloat(min: Float, max: Float): Float = min + (nextFloat() * (max - min));

inline fun Random.nextSign(): Int = if(nextBoolean()) 1 else -1;
