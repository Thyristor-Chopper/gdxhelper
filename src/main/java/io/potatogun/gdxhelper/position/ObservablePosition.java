package io.potatogun.gdxhelper.position;

import com.badlogic.gdx.utils.Array;

import io.potatogun.gdxhelper.function.FloatBiConsumer;

/**
 * 위치(평면좌표)를 저장하는 좌표변경을 감지할 수 있는 레코드이다.
 */
public final class ObservablePosition extends MutablePosition {
	private float x;
	private float y;
	private final Array<FloatBiConsumer> changeHandlers = new Array<>(false, 4, FloatBiConsumer[]::new);

	/**
	 * 변경 감지 가능 좌표를 생성한다.
	 *
	 * @param x 처음 X 좌표
	 * @param y 처음 Y 좌표
	 */
	public ObservablePosition(float x, float y) {
		super(x, y);
		this.x = x;
		this.y = y;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public void setX(float x) {
		if(this.x == x) return;
		this.x = x;

		for(int i=0; i<changeHandlers.size; i++)
			changeHandlers.items[i].accept(this.x, y);
	}

	@Override
	public void addX(float toAdd) {
		x += toAdd;

		for(int i=0; i<changeHandlers.size; i++)
			changeHandlers.items[i].accept(x, y);
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setY(float y) {
		if(this.y == y) return;
		this.y = y;

		for(int i=0; i<changeHandlers.size; i++)
			changeHandlers.items[i].accept(x, this.y);
	}

	@Override
	public void addY(float toAdd) {
		y += toAdd;

		for(int i=0; i<changeHandlers.size; i++)
			changeHandlers.items[i].accept(x, y);
	}

	@Override
	public void set(float x, float y) {
		this.x = x;
		this.y = y;

		for(int i=0; i<changeHandlers.size; i++)
			changeHandlers.items[i].accept(this.x, this.y);
	}

	@Override
	public void set(Position position) {
		x = position.getX();
		y = position.getY();

		for(int i=0; i<changeHandlers.size; i++)
			changeHandlers.items[i].accept(x, y);
	}

	@Override
	public void add(float x, float y) {
		this.x += x;
		this.y += y;

		for(int i=0; i<changeHandlers.size; i++)
			changeHandlers.items[i].accept(this.x, this.y);
	}

	/**
	 * 값이 바뀔 때 콜백 함수를 지정한다.
	 *
	 * @param handler 콜백
	 */
	public void attachObserver(FloatBiConsumer handler) {
		changeHandlers.add(handler);
	}

	/**
	 * 값이 바뀔 때 콜백을 해제한다.
	 *
	 * @param handler 해제할 콜백
	 */
	public void detachObserver(FloatBiConsumer handler) {
		changeHandlers.removeValue(handler, true);
	}

	@Override
	public ObservablePosition copy(float x, float y) {
		return new ObservablePosition(x, y);
	}
}
