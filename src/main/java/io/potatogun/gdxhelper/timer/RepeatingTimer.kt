package io.potatogun.gdxhelper.timer;

import java.util.function.BooleanSupplier;

/**
 * 일정 시간마다 특정 작업을 실행하게 해 주는 타이머
 *
 * 초당 프레임률이 낮을 경우 오차가 누적되며 실행이 누락될 수 있음에 주의할 것.
 *
 * @constructor 조건이 있는 타이머
 * @param interval  실행 간격(초) - 0이면 매 프레임 실행된다.
 * @param condition 실행 조건
 * @param operation 실행할 서브루틴
 */
class RepeatingTimer(interval: Float, condition: BooleanSupplier?, operation: Runnable) : Timer(interval, condition, operation) {
	/**
	 * 조건 없는 타이머를 생성한다.
	 *
	 * @constructor 조건이 없는 타이머
	 * @param interval  대기 시간(초)
	 * @param operation 실행할 서브루틴
	 */
	constructor(interval: Float, operation: Runnable) : this(interval, null, operation);

	/**
	 * 바로 다음 프레임에 실행되는 타이머를 생성한다.
	 *
	 * @constructor 대기시간 없이 다음 프레임에 실행되는 타이머
	 * @param operation 실행할 서브루틴
	 */
	constructor(operation: Runnable) : this(0f, null, operation);

	/**
	 * 바로 다음 프레임에 실행되는 조건 없는 타이머를 생성한다.
	 *
	 * @constructor 대기시간 없이 다음 프레임에 조건이 맞으면 실행되는 타이머
	 * @param condition 실행 조건
	 * @param operation 실행할 서브루틴
	 */
	constructor(condition: BooleanSupplier, operation: Runnable) : this(0f, condition, operation);

	override fun update(delta: Float) {
		super.update(delta);
		if(executed)
			reset();
	}
}
