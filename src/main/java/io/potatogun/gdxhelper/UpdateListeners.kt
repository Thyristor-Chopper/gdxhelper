package io.potatogun.gdxhelper;

import com.badlogic.gdx.utils.Array as GdxArray;

import io.potatogun.gdxhelper.collections.WeakMutableSet;
import io.potatogun.gdxhelper.collections.toArray;
import io.potatogun.gdxhelper.util.ArraySuppliers;
import io.potatogun.gdxhelper.util.Updatable;

import java.util.WeakHashMap;
import java.util.function.BooleanSupplier;

/**
 * 게임 화면과 독힙적으로 매 프레임 상태 갱신을 하는 객체 관리자이다.
 */
object UpdateListeners {
	/**
	 * 등록된 리스너
	 */
	private val listeners = WeakMutableSet<Updatable>();
	/**
	 * 갱신 조건
	 */
	private val conditions = WeakHashMap<Updatable, BooleanSupplier>();
	private val iterableClone = GdxArray<Updatable>(false, 128, ArraySuppliers.updatable);

	/**
	 * 등록된 모든 리스너를 갱신한다.
	 *
	 * @param delta 직전 프레임과의 초 간격
	 * @return 처리된 리스너 개수
	 */
	@JvmSynthetic internal fun update(delta: Float): Int {
		var processed = 0;
		listeners.toArray(iterableClone);
		for(i in 0 until iterableClone.size) {
			val listener = iterableClone.items[i];
			if(conditions.get(listener)?.getAsBoolean() ?: true) {
				listener.update(delta);
				processed++;
			}
		}
		iterableClone.clear();
		return processed;
	}

	/**
	 * 리스너를 등록한다.
	 *
	 * @param listener  리스너
	 * @param condition 갱신 조건
	 */
	@JvmStatic @JvmOverloads fun register(listener: Updatable, condition: BooleanSupplier? = null) {
		listeners.add(listener);
		if(condition != null)
			conditions.put(listener, condition);
	}

	/**
	 * 리스너를 등록을 해제한다.
	 *
	 * @param listener 리스너
	 */
	@JvmStatic fun unregister(listener: Updatable) {
		listeners.remove(listener);
		conditions.remove(listener);
	}
}
