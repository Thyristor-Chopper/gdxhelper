package io.potatogun.gdxhelper.position;

import com.badlogic.gdx.utils.Array as GdxArray;
import com.badlogic.gdx.utils.ObjectMap;

import io.potatogun.gdxhelper.function.FloatBiConsumer;
import io.potatogun.gdxhelper.world.World;

import kotlin.properties.Delegates;

/**
 * 위치(평면좌표)를 저장하는 좌표변경을 감지할 수 있는 레코드이다.
 *
 * @param world 소속 월드
 * @param x     처음 X 좌표
 * @param y     처음 Y 좌표
 */
class ObservablePosition(world: World, x: Float, y: Float) : MutablePosition(world, x, y) {
	private val changeHandlers = GdxArray<FloatBiConsumer>(false, 2);
	override var x: Float by Delegates.observable(x) { _, _, _ -> invokeObservers() };
	override var y: Float by Delegates.observable(y) { _, _, _ -> invokeObservers() };

	/**
	 * 값이 바뀔 때 콜백 함수를 지정한다.
	 *
	 * @param handler 콜백
	 */
	fun addObserver(handler: FloatBiConsumer) {
		changeHandlers.add(handler);
	}

	/**
	 * 값이 바뀔 때 콜백을 해제한다.
	 *
	 * @param handler 해제할 콜백
	 */
	fun removeObserver(handler: FloatBiConsumer) {
		changeHandlers.removeValue(handler, true);
	}

	private inline fun invokeObservers() {
		for(i in 0 until changeHandlers.size)
			changeHandlers[i].accept(x, y);
	}

	override fun copy(world: World, x: Float, y: Float): ObservablePosition = ObservablePosition(world, x, y);
}
