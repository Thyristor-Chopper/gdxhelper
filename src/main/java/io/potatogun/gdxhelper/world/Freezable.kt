package io.potatogun.gdxhelper.world;

/**
 * 시간 멈춤이 가능한 월드
 */
interface Freezable {
	/**
	 * 현재 시간이 멈춰 있는지 여부
	 * 
	 * 디컴파일해서 자바 코드를 본 결과 isFrozen() getter 함수로 되었다.
	 */
	val isFrozen: Boolean;

	/**
	 * 월드의 시간을 멈춘다.
	 */
	fun freeze();

	/**
	 * 월드의 시간을 다시 흐르게 한다.
	 */
	fun unfreeze();
}
