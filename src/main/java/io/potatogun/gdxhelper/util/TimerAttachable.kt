package io.potatogun.gdxhelper.util;

import io.potatogun.gdxhelper.timer.Timer;

/**
 * '외부'에서 이 객체(개체, 월드 등)에 타이머를 붙일 수 있는 객체
 */
interface TimerAttachable {
	/**
	 * 객체에 타이머를 등록한다.
	 *
	 * @param timer 등록할 타이머
	 */
	fun attachTimer(timer: Timer);

	/**
	 * 객체에 타이머를 등록 해제한다.
	 *
	 * 구현체는 다음 둘 중 하나를 해야 한다.
	 *   i)  attachTimer로 등록한 타이머만 해제 허용
	 *   ii) (i)번을 생략하되 책임지고 객체 내부에서 쓰이는 타이머가 외부에 노출되어 임의로 detachTimer를 호출하는 것을 방지
	 *
	 * @param timer 등록 해제할 타이머
	 */
	fun detachTimer(timer: Timer);
}
