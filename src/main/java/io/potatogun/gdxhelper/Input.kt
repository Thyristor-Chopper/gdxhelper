package io.potatogun.gdxhelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input as GdxInput;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array as GdxArray;

import io.potatogun.gdxhelper.collections.WeakMutableSet;
import io.potatogun.gdxhelper.collections.toArray;
import io.potatogun.gdxhelper.screen.Screen;
import io.potatogun.gdxhelper.util.InputListener;

import java.util.WeakHashMap;

/**
 * 키보드 입력을 편리하게 읽는 도우미
 */
object Input {
	/**
	 * 게임의 인스턴스
	 */
	@JvmField @JvmSynthetic internal var game: Game? = null;
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
	// 입력 이벤트 감지기 관련
	private val inputListeners = WeakMutableSet<InputListener>();
	private val listenerConditions = WeakHashMap<InputListener, ListenerCondition>();
	private val activeScreenListeners = WeakMutableSet<Screen>();  // 현재 화면이 아니어도 입력을 감지할 수 있는 화면
	private val iterableClone = GdxArray<InputListener>(16);  // 순회용 사본

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

	private inline fun forEachListeners(crossinline callback: (InputListener) -> Boolean): Boolean {
		var processed = false;
		inputListeners.toArray(iterableClone);
		for(i in 0 until iterableClone.size) {
			val listener = iterableClone[i];
			val conditionMet = listenerConditions.get(listener)?.test(listener, game!!) ?: true;  // game이 null인 경우는 극히 드물고 정상적인 상황도 아닐 것
			val listenable = (listener !is Screen || activeScreenListeners.contains(listener) || game?.screen === listener);
			if(listenable && conditionMet)
				processed = callback(listener) || processed;
		}
		iterableClone.clear();
		return processed;
	}

	/**
	 * 입력 감시자를 등록한다.
	 *
	 * @param listener  감시자
	 * @param condition 이벤트 수신 조건
	 */
	@JvmStatic @JvmOverloads fun registerListener(listener: InputListener, condition: ListenerCondition? = null) {
		inputListeners.add(listener);
		if(condition != null)
			listenerConditions.put(listener, condition);
	}

	/**
	 * 화면형 입력 감시자를 등록한다.
	 *
	 * @param listener     감시자
	 * @param alwaysActive 거짓이면 현재 화면인 경우에만 감시
	 * @param condition    이벤트 수신 조건
	 */
	@JvmStatic @JvmOverloads fun <ScreenInputListener> registerListener(listener: ScreenInputListener, alwaysActive: Boolean = false, condition: ListenerCondition? = null) where ScreenInputListener : InputListener, ScreenInputListener : Screen {
		registerListener(listener, condition);
		if(alwaysActive)
			activeScreenListeners.add(listener);
	}

	/**
	 * 입력 감시자를 등록 해제한다.
	 *
	 * @param listener 감시자
	 */
	@JvmStatic fun unregisterListener(listener: InputListener) {
		inputListeners.remove(listener);
		listenerConditions.remove(listener);
		if(listener is Screen)
			activeScreenListeners.remove(listener);
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

	/**
	 * 입력 감시자가 이벤트를 수신할 수 있는지를 반환하는 람다함수
	 */
	fun interface ListenerCondition {
		fun test(listener: InputListener, game: Game): Boolean;
	}
}
