package io.potatogun.gdxhelper.util;

/**
 * 매 프레임 상태를 갱신하는 객체
 */
interface Updatable {
	/**
	 * 매 프레임 호출되어 상태를 갱신한다.
	 *
	 * @param delta 직전 프레임과의 시간 간격(초). 60fps면 약 0.0167이다.
	 */
	fun update(delta: Float);
}
