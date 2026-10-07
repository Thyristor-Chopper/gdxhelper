package io.potatogun.gdxhelper.position;

import io.potatogun.gdxhelper.entity.Entity;

// 원래는 data class Position(x, y)가 있고 MutablePosition이 Position을 상속하게 하려 했으나 
//   레코드는 상속이 불가능해서 복잡하지만 이렇게 했다.

/**
 * 위치(평면좌표)에 대한 객체이다.
 */
public class Position {
	/**
	 * 개체의 X 좌표
	 */
	protected float x;
	/**
	 * 개체의 Y 좌표
	 */
	protected float y;

	/**
	 * 좌표객체를 생성한다.
	 *
	 * @param x X 좌표
	 * @param y Y 좌표
	 */
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * X 좌표를 가져온다.
	 *
	 * @return X 좌표
	 */
	public final float getX() {
		return x;
	}

	/**
	 * Y 좌표를 가져온다.
	 *
	 * @return Y 좌표
	 */
	public final float getY() {
		return y;
	}

	/**
	 * 두 위치 사이의 거리를 구한다.
	 *
	 * @param other 다른 위치
	 * @return 거리
	 */
	public final float distanceTo(Position other) {
		final float dx = x - other.getX();
		final float dy = y - other.getY();
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	@Override
	public final boolean equals(Object other) {
		if(!(other instanceof Position)) return false;
		final Position otherPosition = (Position) other;
		return otherPosition.getX() == x && otherPosition.getY() == y;
	}

	@Override
	public int hashCode() {
		int hash = Float.hashCode(x);
		hash = 31 * hash + Float.hashCode(y);
		return hash;
	}

	/**
	 * 개체의 메모리 주소 기반 해시코드를 가져온다.
	 *
	 * @return identity hash code
	 */
	protected int identityHashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public final float component1() {
		return x;
	}

	public final float component2() {
		return y;
	}
}
