package io.potatogun.gdxhelper.util;

import com.badlogic.gdx.utils.ArraySupplier;

import io.potatogun.gdxhelper.entity.Entity;
import io.potatogun.gdxhelper.timer.Timer;
import io.potatogun.gdxhelper.widget.Widget;

/**
 * libGDX 특정 자료형 배열의 생성자 모음이다. 코틀린은 ::new가 안 되므로 자바로 작성되었다.
 */
public final class ArraySuppliers {
	public static final ArraySupplier<Entity[]> entity = Entity[]::new;
	public static final ArraySupplier<InputListener[]> inputListener = InputListener[]::new;
	public static final ArraySupplier<Runnable[]> runnable = Runnable[]::new;
	public static final ArraySupplier<Timer[]> timer = Timer[]::new;
	public static final ArraySupplier<Updatable[]> updatable = Updatable[]::new;
	public static final ArraySupplier<Widget[]> widget = Widget[]::new;

	private ArraySuppliers() {
		throw new UnsupportedOperationException("this class cannot be instantiated");
	}
}
