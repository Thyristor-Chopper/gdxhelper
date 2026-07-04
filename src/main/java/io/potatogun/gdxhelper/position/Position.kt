package io.potatogun.gdxhelper.position;

import io.potatogun.gdxhelper.entity.Entity;
import io.potatogun.gdxhelper.world.World;

import kotlin.math.sqrt;

// 원래는 data class Position(x, y)가 있고 MutablePosition이 Position을 상속하게 하려 했으나 
//   레코드는 상속이 불가능해서 복잡하지만 이렇게 했다.

/**
 * 위치(평면좌표)에 대한 객체이다.
 *
 * @property world 소속 월드
 * @property x     X 좌표
 * @property y     Y 좌표
 */
open class Position(val world: World, open val x: Float, open val y: Float) {
	/**
	 * 두 위치 사이의 거리를 구한다.
	 *
	 * @param other 다른 위치
	 * @return 거리
	 */
	fun distanceTo(other: Position): Float {
		val dx = x - other.x;
		val dy = y - other.y;
		return sqrt(dx * dx + dy * dy);
	}

	override fun equals(other: Any?) = other is Position && other.world === world && other.x == x && other.y == y;

	override fun hashCode(): Int {
		// return Objects.hash(world, x, y);
		var hash = world.hashCode();
		hash = 31 * hash + x.hashCode();
		hash = 31 * hash + y.hashCode();
		return hash;
	}

	override fun toString(): String = "$world($x, $y)";

	open fun copy(world: World = this.world, x: Float = this.x, y: Float = this.y): Position = Position(world, x, y);

	fun component1(): World = world;

	fun component2(): Float = x;

	fun component3(): Float = y;
}
