package io.potatogun.gdxhelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input as GdxInput;
import com.badlogic.gdx.InputProcessor;

import io.potatogun.gdxhelper.collections.WeakMutableSet;
import io.potatogun.gdxhelper.util.InputListener;

/**
 * 키보드 입력을 편리하게 읽는 도우미
 */
object Input {
	/**
	 * 현재 반디의 X 좌표
	 */
	@JvmStatic val mouseX: Int
		inline get() = Gdx.input.getX();
	/**
	 * 현재 반디의 Y 좌표
	 */
	@JvmStatic val mouseY: Int
		inline get() = Gdx.input.getY();
	private val inputListeners = WeakMutableSet<InputListener>();

	init {
		// https://stackoverflow.com/questions/17644429/libgdx-mouse-just-clicked 참고함
		Gdx.input.setInputProcessor(object : InputProcessor {
			override fun scrolled(amountX: Float, amountY: Float): Boolean = forEachListeners { it.onScroll(amountX, amountY) };

			override fun keyDown(code: Int): Boolean = forEachListeners { it.onKeyDown(code) };

			override fun keyUp(code: Int): Boolean = forEachListeners { it.onKeyUp(code) };

			override fun mouseMoved(x: Int, y: Int): Boolean = forEachListeners { it.onMouseMove(x, y) };

			override fun touchDragged(x: Int, y: Int, pointer: Int): Boolean = forEachListeners { it.onTouchDrag(x, y, pointer) };

			override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean = forEachListeners { it.onTouchDown(x, y, pointer, button) };

			override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean = forEachListeners { it.onTouchUp(x, y, pointer, button) };

			override fun touchCancelled(x: Int, y: Int, pointer: Int, button: Int): Boolean = forEachListeners { it.onTouchCancel(x, y, pointer, button) };
			
			override fun keyTyped(char: Char): Boolean = forEachListeners { it.onKeyType(char) };
		});
	}

	private inline fun forEachListeners(callback: (InputListener) -> Boolean): Boolean {
		var processed = false;
		val iterator = inputListeners.iterator();
		while(iterator.hasNext()) {
			val listener = iterator.next();
			processed = callback(listener) || processed;
		}
		return processed;
	}

	fun registerListener(listener: InputListener) {
		inputListeners.add(listener);
	}

	fun unregisterListener(listener: InputListener) {
		inputListeners.remove(listener);
	}

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
