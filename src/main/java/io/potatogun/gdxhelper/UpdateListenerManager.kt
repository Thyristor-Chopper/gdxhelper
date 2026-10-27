package io.potatogun.gdxhelper;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.Disposable;

import io.potatogun.gdxhelper.collections.WeakMutableSet;
import io.potatogun.gdxhelper.collections.toArray;
import io.potatogun.gdxhelper.util.ArraySuppliers;
import io.potatogun.gdxhelper.util.Updatable;

import java.util.WeakHashMap;

/**
 * 게임 화면과 독립적으로 매 프레임 상태 갱신을 하는 객체 관리자이다.
 */
class UpdateListenerManager(private val game: ApplicationListener) : Updatable, Disposable {
	/**
	 * 등록된 리스너
	 */
	private val listeners = WeakMutableSet<Updatable>();
	/**
	 * 갱신 조건
	 */
	private val conditions = WeakHashMap<Updatable, ListenerCondition>();
	// 순회용 사본
	private val iterableClone = GdxArray<Updatable>(false, 128, ArraySuppliers.updatable);

	/**
	 * 등록된 모든 리스너를 갱신한다.
	 *
	 * @param delta 직전 프레임과의 초 간격
	 */
	override fun update(delta: Float) {
		if(listeners.isEmpty()) return;
		listeners.toArray(iterableClone);
		for(i in 0 until iterableClone.size) {
			val listener = iterableClone.items[i];
			if(conditions.get(listener)?.test(listener, game) ?: true)
				listener.update(delta);
		}
		iterableClone.clear();
	}

	/**
	 * 리스너를 등록한다.
	 *
	 * @param listener  리스너
	 * @param condition 갱신 조건
	 */
	@JvmOverloads fun register(listener: Updatable, condition: ListenerCondition? = null) {
		listeners.add(listener);
		if(condition != null)
			conditions.put(listener, condition);
	}

	/**
	 * 리스너를 등록을 해제한다.
	 *
	 * @param listener 리스너
	 */
	fun unregister(listener: Updatable) {
		listeners.remove(listener);
		conditions.remove(listener);
	}

	override fun dispose() {
		listeners.clear();
		conditions.clear();
	}

	/**
	 * 리스너가 이번에 상태갱신이 가능한지를 반환하는 람다함수
	 */
	fun interface ListenerCondition {
		fun test(listener: Updatable, game: ApplicationListener): Boolean;
	}
}
