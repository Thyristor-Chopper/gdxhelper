package io.potatogun.gdxhelper;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array as GdxArray;

import io.potatogun.gdxhelper.collections.WeakMutableSet;
import io.potatogun.gdxhelper.collections.toArray;
import io.potatogun.gdxhelper.screen.Screen;
import io.potatogun.gdxhelper.util.ArraySuppliers;
import io.potatogun.gdxhelper.util.InputListener;
import io.potatogun.gdxhelper.util.getGameInstance;

import java.util.WeakHashMap;

/**
 * 입력 감시자 관리자
 */
object InputListeners : InputProcessor {
	// 입력 이벤트 감지기 관련
	private val inputListeners = WeakMutableSet<InputListener>();
	private val listenerConditions = WeakHashMap<InputListener, ListenerCondition>();
	private val activeScreenListeners = WeakMutableSet<Screen>();  // 현재 화면이 아니어도 입력을 감지할 수 있는 화면
	private val iterableClone = GdxArray<InputListener>(false, 16, ArraySuppliers.inputListener);  // 순회용 사본

	init {
		Gdx.input.setInputProcessor(this);
	}

	/**
	 * 입력 감시자를 등록한다.
	 *
	 * @param listener  감시자
	 * @param condition 이벤트 수신 조건
	 */
	@JvmOverloads fun register(listener: InputListener, condition: ListenerCondition? = null) {
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
	@JvmOverloads fun <ScreenInputListener> register(listener: ScreenInputListener, alwaysActive: Boolean = false, condition: ListenerCondition? = null) where ScreenInputListener : InputListener, ScreenInputListener : Screen {
		register(listener, condition);
		if(alwaysActive)
			activeScreenListeners.add(listener);
	}

	/**
	 * 입력 감시자를 등록 해제한다.
	 *
	 * @param listener 감시자
	 */
	fun unregister(listener: InputListener) {
		inputListeners.remove(listener);
		listenerConditions.remove(listener);
		if(listener is Screen)
			activeScreenListeners.remove(listener);
	}

	override fun scrolled(amountX: Float, amountY: Float): Boolean = forEachListeners { it.onScroll(amountX, amountY) };

	override fun keyDown(code: Int): Boolean = forEachListeners { it.onKeyDown(code) };

	override fun keyUp(code: Int): Boolean = forEachListeners { it.onKeyUp(code) };

	override fun mouseMoved(x: Int, y: Int): Boolean = forEachListeners { it.onMouseMove(x, y) };

	override fun touchDragged(x: Int, y: Int, pointer: Int): Boolean = forEachListeners { it.onTouchDrag(x, y, pointer) };

	override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean = forEachListeners { it.onTouchDown(x, y, pointer, button) };

	override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean = forEachListeners { it.onTouchUp(x, y, pointer, button) };

	override fun touchCancelled(x: Int, y: Int, pointer: Int, button: Int): Boolean = forEachListeners { it.onTouchCancel(x, y, pointer, button) };
	
	override fun keyTyped(char: Char): Boolean = forEachListeners { it.onKeyType(char) };

	private inline fun forEachListeners(crossinline callback: (InputListener) -> Boolean): Boolean {
		val game = getGameInstance();
		var processed = false;
		inputListeners.toArray(iterableClone);
		for(i in 0 until iterableClone.size) {
			val listener = iterableClone.items[i];
			val conditionMet = listenerConditions.get(listener)?.test(listener, game) ?: true;
			val listenable = (listener !is Screen || activeScreenListeners.contains(listener) || game.screen === listener);
			if(listenable && conditionMet)
				processed = callback(listener) || processed;
		}
		iterableClone.clear();
		return processed;
	}

	/**
	 * 입력 감시자가 이벤트를 수신할 수 있는지를 반환하는 람다함수
	 */
	fun interface ListenerCondition {
		fun test(listener: InputListener, game: ApplicationListener): Boolean;
	}
}
