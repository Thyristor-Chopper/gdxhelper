package io.potatogun.gdxhelper.world;

/**
 * 시간 멈춤의 가장 기본적인 구현
 *
 * 단독으로 사용하지 않고 위임으로만 사용한다.
 */
class SimpleFreezer : Freezable {
	override var isFrozen = false
		private set;

	override fun freeze() {
		isFrozen = true;
	}

	override fun unfreeze() {
		isFrozen = false;
	}
}
