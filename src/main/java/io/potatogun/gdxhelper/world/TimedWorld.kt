package io.potatogun.gdxhelper.world;

/**
 * 자체 시각이 있는 월드
 *
 * 구현 방식이나 time의 단위는 구현자 나름이다.
 *
 * 가장 일반적인 구현은 time을 초로 두고 update에서 time += delta를 하는 것일 거다.
 */
interface TimedWorld {
	/**
	 * 현재 월드의 시각
	 *
	 * 월드 생성 이후 지난 시간이다.
	 */
	val time: Float;
}
