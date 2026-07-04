package io.potatogun.gdxhelper.pools;

import com.badlogic.gdx.utils.Pool;

import io.potatogun.gdxhelper.position.MutablePosition;

/**
 * 수정 가능한 좌표 풀
 */
abstract class MutablePositionPool() : Pool<MutablePosition>() {
	/**
	 * 지정한 좌표값의 수정 가능한 좌표를 가져온다.
	 */
	inline fun obtain(xPos: Float, yPos: Float): MutablePosition = obtain().apply {
		x = xPos;
		y = yPos;
	};
}
