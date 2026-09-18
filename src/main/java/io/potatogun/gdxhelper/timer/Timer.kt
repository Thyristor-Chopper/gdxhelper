package io.potatogun.gdxhelper.timer;

import java.util.function.BooleanSupplier;

/**
 * 지정된 시간 후 특정 작업을 실행하게 해 주는 타이머
 *
 * @constructor 조건이 있는 타이머
 * @property delay     대기 시간(초)
 * @property condition 실행 조건
 * @property operation 실행할 서브루틴
 */
open class Timer(private val delay: Float, @JvmSynthetic internal val condition: BooleanSupplier?, private val operation: Runnable) {
	private var timer = delay
		set(value) {
			if(value < 0f) field = 0f;
			else field = value;
		};
	/**
	 * 타이머가 실행되었는지의 여부
	 */
	@get:JvmName("hasExecuted")
	var executed = false
		private set;

	/**
	 * 조건 없는 타이머를 생성한다.
	 *
	 * @constructor 조건이 없는 타이머
	 * @param delay     대기 시간(초)
	 * @param operation 실행할 서브루틴
	 */
	constructor(delay: Float, operation: Runnable) : this(delay, null, operation);

	/**
	 * 타이머를 갱신한다.
	 *
	 * @param delta 직전 프레임과의 시간 간격(초)
	 */
	@JvmSynthetic internal open fun tick(delta: Float) {
		timer -= delta;
		if(timer == 0f) {
			operation.run();
			executed = true;
		}
	}

	/**
	 * 대기 시간을 초기화한다.
	 */
	fun reset() {
		timer = delay;
		executed = false;
	}
}
