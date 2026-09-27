package io.potatogun.gdxhelper.timer;

import io.potatogun.gdxhelper.util.Updatable;

import java.util.function.BooleanSupplier;

/**
 * 지정된 시간 후 특정 작업을 실행하게 해 주는 타이머
 *
 * @constructor 조건이 있는 타이머
 * @property timeout   대기 시간(초)
 * @property condition 실행 조건
 * @property operation 실행할 서브루틴
 */
open class Timer(@JvmField protected var timeout: Float, @JvmField @JvmSynthetic internal val condition: BooleanSupplier?, @JvmField protected val operation: Runnable) : Updatable {
	@JvmField protected var timer = timeout;
	/**
	 * 타이머가 실행되었는지의 여부
	 */
	@get:JvmName("hasExecuted")
	var executed = false
		protected set;

	/**
	 * 조건 없는 타이머를 생성한다.
	 *
	 * @constructor 조건이 없는 타이머
	 * @param timeout   대기 시간(초)
	 * @param operation 실행할 서브루틴
	 */
	constructor(timeout: Float, operation: Runnable) : this(timeout, null, operation);

	/**
	 * 타이머를 갱신한다.
	 *
	 * @param delta 직전 프레임과의 시간 간격(초)
	 */
	override fun update(delta: Float) {
		if(executed) return;
		timer -= delta;
		if(timer <= 0f) {
			executed = true;
			operation.run();
			timer = 0f;
		}
	}

	/**
	 * 대기 시간을 초기화한다.
	 */
	fun reset() {
		timer = timeout;
		executed = false;
	}

	/**
	 * 타이머의 대기 시간을 변경한다.
	 *
	 * 주의: 변경된 대기 시간은 이번 실행 이후 다시 초기화(reset)한 이후부터 적용된다.
	 *
	 * @param timeout 새 대기 시간
	 */
	fun setTimeout(timeout: Float) {
		this.timeout = timeout;
	}
}
