package io.potatogun.gdxhelper;

import com.badlogic.gdx.Gdx;

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

	/**
	 * 창 크기 캐시를 최신화한다.
	 *
	 * @param intWidth  정수 너비
	 * @param intHeight 정수 높이
	 */
	@JvmSynthetic internal fun updateWindowSize(intWidth: Int, intHeight: Int) {
		this.intWidth = intWidth;
		this.intHeight = intHeight;
		this.width = intWidth.toFloat();
		this.height = intHeight.toFloat();
	}
}
