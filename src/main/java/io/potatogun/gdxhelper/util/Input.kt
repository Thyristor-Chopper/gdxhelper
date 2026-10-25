package io.potatogun.gdxhelper.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input as GdxInput;

import io.potatogun.gdxhelper.Window;

/**
 * 키보드 입력을 편리하게 읽는 도우미
 */
object Input {
	/**
	 * 다람쥐의 X 좌표
	 */
	@JvmStatic val mouseX: Int
		inline get() = (Gdx.input.x * xMultiplier).toInt();
	/**
	 * 다람쥐의 Y 좌표
	 */
	@JvmStatic val mouseY: Int
		inline get() = (Gdx.input.y * yMultiplier).toInt();
	@set:JvmSynthetic var xMultiplier = 1f
		internal set;
	@set:JvmSynthetic var yMultiplier = 1f
		internal set;
	@JvmStatic val isTouched: Boolean
		inline get() = Gdx.input.isTouched();
	@JvmStatic val isJustTouched: Boolean
		inline get() = Gdx.input.justTouched();

	/**
	 * 키가 현재 '눌려 있는 중' 인지 — 꾹 누르고 있으면 매 프레임 true.
	 *   이동(← → ↑ ↓) 처럼 '누르는 동안 계속' 일어나야 할 동작에 사용.
	 *
	 * @param key 글쇠 번호
	 * @return 눌렸으면 true
	 */
	@JvmStatic inline fun isKeyPressed(key: Int): Boolean = Gdx.input.isKeyPressed(key);

	/**
	 * 키가 '이번 프레임에 막 눌렸는지' — 꾹 눌러도 첫 프레임에만 true.
	 *   총알 발사, 메뉴 선택처럼 '한 번만' 실행되어야 할 동작에 사용.
	 *
	 * @param key 글쇠 번호
	 * @return 눌렸으면 true
	 */
	@JvmStatic inline fun isKeyJustPressed(key: Int): Boolean = Gdx.input.isKeyJustPressed(key);

	/**
	 * 지정한 마우스 단추가 눌려 있는지의 여부
	 *
	 * @param button 단추의 종류
	 * @return 눌렸으면 true
	 */
	@JvmStatic inline fun isButtonPressed(button: Int): Boolean = Gdx.input.isButtonPressed(button);

	/**
	 * 지정한 마우스 단추를 막 눌렀는지의 여부
	 *
	 * @param button 단추의 종류
	 * @return 눌렀으면 true
	 */
	@JvmStatic inline fun isButtonJustPressed(button: Int): Boolean = Gdx.input.isButtonJustPressed(button);

	@JvmStatic inline fun isTouched(pointer: Int): Boolean = Gdx.input.isTouched(pointer);

	@JvmStatic inline fun mouseX(pointer: Int): Int = (Gdx.input.getX(pointer) * xMultiplier).toInt();

	@JvmStatic inline fun mouseY(pointer: Int): Int = (Gdx.input.getY(pointer) * yMultiplier).toInt();

	// 자주 쓰는 키 상수를 짧은 이름으로 재노출.
	//   원본은 Input.Keys.LEFT 처럼 길어서 자주 쓸수록 번거롭다.
	//   필요하면 Input.Keys.XXX 에서 다른 키를 직접 import 해서 써도 된다.
	const val LEFT = GdxInput.Keys.LEFT;
	const val RIGHT = GdxInput.Keys.RIGHT;
	const val UP = GdxInput.Keys.UP;
	const val DOWN = GdxInput.Keys.DOWN;
	const val SPACE = GdxInput.Keys.SPACE;
	const val ESCAPE = GdxInput.Keys.ESCAPE;
	const val W = GdxInput.Keys.W;
	const val A = GdxInput.Keys.A;
	const val S = GdxInput.Keys.S;
	const val D = GdxInput.Keys.D;
	const val P = GdxInput.Keys.P;
	const val R = GdxInput.Keys.R;
	const val DELETE = GdxInput.Keys.FORWARD_DEL;
	const val BACKTICK = GdxInput.Keys.GRAVE;
	const val SHIFT_LEFT = GdxInput.Keys.SHIFT_LEFT;
	const val LEFT_MOUSE = GdxInput.Buttons.LEFT;
	const val RIGHT_MOUSE = GdxInput.Buttons.RIGHT;
}
