package io.potatogun.gdxhelper;

import com.badlogic.gdx.Gdx;

import io.potatogun.gdxhelper.util.Input;
import io.potatogun.gdxhelper.util.getGameInstance;
import io.potatogun.gdxhelper.util.takeIfInstance;

/**
 * 게임 화면(창)의 정보
 */
object Window {
	// 창 크기 캐시
	/**
	 * 현재 창의 너비
	 */
	@get:JvmStatic var width = 0f  // lateinit이 불가하여 0으로 초기화
		private set;
	/**
	 * 현재 창의 높이
	 */
	@get:JvmStatic var height = 0f
		private set;
	/**
	 * 현재 창의 너비 (정수형)
	 */
	@get:JvmStatic var intWidth = 0
		private set;
	/**
	 * 현재 창의 높이 (정수형)
	 */
	@get:JvmStatic var intHeight = 0
		private set;
	@get:JvmSynthetic internal var fixedWidth = 0
		private set;
	@get:JvmSynthetic internal var fixedHeight = 0
		private set;
	@get:JvmStatic var realWidth = 0
		private set;
	@get:JvmStatic var realHeight = 0
		private set;

	init {
		updateWindowSize(Gdx.graphics.width, Gdx.graphics.height);
	}

	/**
	 * 창 제목을 변경한다.
	 *
	 * @param title 전체 제목
	 */
	@JvmStatic inline fun setTitle(title: String) {
		Gdx.graphics.setTitle(title);
	}

	fun setFixedSize(width: Int, height: Int) {
		if(width < 0 || height < 0)
			throw IllegalArgumentException("invalid dimensions");

		fixedWidth = width;
		fixedHeight = height;
		updateWindowSize(Gdx.graphics.width, Gdx.graphics.height);
		getGameInstance().resize(width, height);
	}

	/**
	 * 창 크기 캐시를 최신화한다.
	 *
	 * @param intWidth  정수 너비
	 * @param intHeight 정수 높이
	 */
	@JvmSynthetic internal fun updateWindowSize(intWidth: Int, intHeight: Int) {
		if(fixedWidth == 0 && fixedHeight == 0) {
			this.intWidth = intWidth;
			this.intHeight = intHeight;
			this.width = intWidth.toFloat();
			this.height = intHeight.toFloat();
			Input.xMultiplier = 1f;
			Input.yMultiplier = 1f;
		} else {
			this.intWidth = fixedWidth;
			this.intHeight = fixedHeight;
			this.width = fixedWidth.toFloat();
			this.height = fixedHeight.toFloat();
			Input.xMultiplier = fixedWidth.toFloat() / intWidth.toFloat();
			Input.yMultiplier = fixedHeight.toFloat() / intHeight.toFloat();
		}
		this.realWidth = intWidth;
		this.realHeight = intHeight;
	}
}
