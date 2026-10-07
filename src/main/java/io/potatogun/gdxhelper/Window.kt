package io.potatogun.gdxhelper;

import com.badlogic.gdx.Gdx;

import kotlin.properties.Delegates;

/**
 * 게임 화면(창)의 정보
 */
object Window {
	// 창 크기 캐시
	/**
	 * 현재 창의 너비
	 */
	@JvmStatic var width = 0f  // lateinit이 불가하여 0으로 초기화
		private set;
	/**
	 * 현재 창의 높이
	 */
	@JvmStatic var height = 0f
		private set;
	/**
	 * 현재 창의 너비 (정수형)
	 */
	@JvmStatic var intWidth = 0
		private set;
	/**
	 * 현재 창의 높이 (정수형)
	 */
	@JvmStatic var intHeight = 0
		private set;
	/**
	 * 제목 표시줄 제목의 base
	 */
	private var titleBarBase: String by Delegates.observable("") { _, _, _ -> updateTitle() };
	/**
	 * 제목 표시줄 제목에 표시할 상태 정보
	 */
	@JvmStatic var titleBarInfo: String? by Delegates.observable(null) { _, _, _ -> updateTitle() };
	/**
	 * 제목 표시줄 제목에 표시할 통계 정보
	 */
	@JvmStatic var titleBarStats: String? by Delegates.observable(null) { _, _, _ -> updateTitle() };

	init {
		updateWindowDimensions();
	}

	/**
	 * 창 제목을 직접 변경한다.
	 * @param title 전체 제목
	 */
	@JvmStatic inline fun setTitle(title: String) {
		Gdx.graphics.setTitle(title);
	}

	/**
	 * 제목 표시줄 제목의 base를 지정한다.
	 *
	 * @param value base title
	 */
	@JvmStatic fun setBaseTitle(value: String?) {
		if(value == null)
			titleBarBase = "";
		else
			titleBarBase = value;
	}

	/**
	 * 제목 표시줄 제목을 갱신한다.
	 */
	private fun updateTitle() {
		val titleBarInfo = this.titleBarInfo?.let { " - $it" } ?: "";
		val titleBarStats = this.titleBarStats?.let { " / $it" } ?: "";
		setTitle("${titleBarBase}${titleBarInfo}${titleBarStats}");
	}

	/**
	 * 창 크기 캐시를 최신화한다.
	 */
	@JvmSynthetic internal fun updateWindowSize(intWidth: Int, intHeight: Int) {
		this.intWidth = intWidth;
		this.intHeight = intHeight;
		this.width = intWidth.toFloat();
		this.height = intHeight.toFloat();
	}
}
