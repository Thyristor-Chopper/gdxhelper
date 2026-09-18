package io.potatogun.gdxhelper.timer;

import java.util.function.BooleanSupplier;

/**
 * 일정 시간마다 특정 작업을 실행하게 해 주는 타이머
 *
 * @constructor 조건이 있는 타이머
 * @param interval  실행 간격(초)
 * @param condition 실행 조건
 * @param operation 실행할 서브루틴
 */
class RepeatingTimer(interval: Float, condition: BooleanSupplier?, operation: Runnable) : Timer(interval, condition, operation) {
	/**
	 * 조건 없는 타이머를 생성한다.
	 *
	 * @constructor 조건이 없는 타이머
	 * @param delay     대기 시간(초)
	 * @param operation 실행할 서브루틴
	 */
	constructor(interval: Float, operation: Runnable) : this(interval, null, operation);

	override fun tick(delta: Float) {
		super.tick(delta);
		if(executed) reset();
	}
}
