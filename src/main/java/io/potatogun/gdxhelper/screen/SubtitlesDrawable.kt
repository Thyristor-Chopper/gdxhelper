package io.potatogun.gdxhelper.screen;

import com.badlogic.gdx.graphics.Color;

/**
 * '자막'을 그릴 수 있는 화면.
 *   자막이 어떤 모습으로 그려지는지는 구현체 나름이다.
 */
interface SubtitlesDrawable {
	/**
	 * 기본 자막 표시 시간
	 */
	val subtitlesDuration: Float;
	/**
	 * 기본 자막 글자 색
	 */
	val subtitlesColor: Color;

	/**
	 * 화면에 자막을 표시한다.
	 *
	 * @param message 표시할 내용
	 */
	fun drawSubtitles(message: String) {
		drawSubtitles(message, subtitlesDuration, subtitlesColor);
	}

	/**
	 * 화면에 자막을 표시한다.
	 *
	 * @param message  표시할 내용
	 * @param duration 표시 시간(초)
	 */
	fun drawSubtitles(message: String, duration: Float) {
		drawSubtitles(message, duration, subtitlesColor);
	}

	/**
	 * 화면에 자막을 표시한다.
	 *
	 * @param message 표시할 내용
	 * @param color   글자 색
	 */
	fun drawSubtitles(message: String, color: Color) {
		drawSubtitles(message, subtitlesDuration, color);
	}

	/**
	 * 화면에 자막을 표시한다.
	 *
	 * @param message  표시할 내용
	 * @param duration 표시 시간(초)
	 * @param color    글자 색
	 */
	fun drawSubtitles(message: String, duration: Float, color: Color);
}
