package io.potatogun.gdxhelper.util;

/**
 * 입력을 감지하는 객체
 */
interface InputListener {
	fun onScroll(amountX: Float, amountY: Float): Boolean = false;

	fun onKeyDown(code: Int): Boolean = false;

	fun onKeyUp(code: Int): Boolean = false;

	fun onMouseMove(x: Int, y: Int): Boolean = false;

	fun onTouchDrag(x: Int, y: Int, pointer: Int): Boolean = false;

	fun onTouchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean = false;

	fun onTouchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean = false;

	fun onTouchCancel(x: Int, y: Int, pointer: Int, button: Int): Boolean = false;
	
	fun onKeyType(char: Char): Boolean = false;
}
