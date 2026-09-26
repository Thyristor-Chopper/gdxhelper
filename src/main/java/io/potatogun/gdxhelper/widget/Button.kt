package io.potatogun.gdxhelper.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import io.potatogun.gdxhelper.function.FloatSupplier;
import io.potatogun.gdxhelper.util.Input;
import io.potatogun.gdxhelper.util.drawText;
import io.potatogun.gdxhelper.Window;

/**
 * 단추 - 코틀린용 생성자이며 자바 개발자라면 빌더를 사용하면 된다.
 *
 * x과 y 위치는 screenWidth 등이 포함될 경우 창 크기가 바뀔 때마다 값이 달라지므로 람다로 받는다.
 *
 * @constructor 동적 위치를 사용하는 생성자
 * @param    x       X 좌표 계산 함수
 * @param    y       Y 좌표 계산 함수
 * @param    width   단추 너비 계산 함수
 * @param    height  단추 높이 계산 함수
 * @param    caption 단추 라벨
 * @property skin    단추의 스킨(텍스처 묶음)
 * @property tint    단추의 오버레이 색(흰색: 변경없음)
 * @property onClick 단추를 눌렀을 때 실행할 서브루틴
 */
class Button(x: FloatSupplier, y: FloatSupplier, width: FloatSupplier, height: FloatSupplier = { 25f }, caption: String, private val skin: Skin, private val tint: Color = Color.WHITE, private val onClick: Runnable = {}) : Widget(x, y, width, height) {
	companion object {
		private val DEFAULT_CAPTION_COLOR = Color.BLACK;
		private val DEFAULT_DISABLED_CAPTION_COLOR = Color.GRAY;
	}

	private val font = BitmapFont();
	private val accessKey: Char?;
	private val caption: String;
	private var previouslyPressed = false;
	private var isEnabled = true;
	private var isHover = false;
	private var isPressed = false;

	/**
	 * 단추 - 코틀린용 생성자이며 자바 개발자라면 빌더를 사용하면 된다.
	 *
	 * @constructor 정적 위치를 사용하는 생성자
	 * @param x       X 좌표
	 * @param y       Y 좌표
	 * @param width   단추 너비
	 * @param height  단추 높이
	 * @param caption 단추 라벨
	 * @param skin    단추의 스킨(텍스처 묶음)
	 * @param tint    단추의 색 오버레이
	 * @param onClick 단추를 눌렀을 때 실행할 서브루틴
	 */
	constructor(x: Float, y: Float, width: Float, height: Float = 25f, caption: String, skin: Skin, tint: Color = Color.WHITE, onClick: Runnable = {}) : this({ x }, { y }, { width }, { height }, caption, skin, tint, onClick);

	init {
		val accessKeyMatch = Regex("[&]([A-Za-z])");
		this.accessKey = accessKeyMatch.find(caption)?.value?.get(1)?.uppercaseChar();
		this.caption = caption.replaceFirst(accessKeyMatch, "$1");
	}

	override fun update(delta: Float) {
		val x = getX();
		val y = getY();
		val width = getWidth();
		val height = getHeight();

		val mouseX = Input.mouseX.toFloat();
		val mouseY = Window.height - Input.mouseY;
		isHover = isEnabled && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
		isPressed = isEnabled && isHover && Input.isButtonPressed(Input.LEFT_MOUSE);

		// 마우스를 눌렀다 뗐으면서 뗀 순간에 반디가 단추 위에 있으면 클릭 이벤트 발생
		if(!isEnabled || !isVisible) {
			previouslyPressed = false;
		} else if(isPressed) {
			previouslyPressed = true;
		} else if(isHover && previouslyPressed) {
			fireClickEvent();
		} else {
			previouslyPressed = false;
		}

		detectAccessKeyPress();
	}

	override fun draw(batch: SpriteBatch) {
		val x = getX();
		val y = getY();
		val width = getWidth();
		val height = getHeight();

		val fontColor =
			if(!isEnabled)
				skin.disabledCaptionColor
			else if(isPressed)
				skin.pressedCaptionColor
			else if(isHover)
				skin.hoverCaptionColor
			else
				skin.captionColor;

		val texture =
			if(!isEnabled)
				skin.disabled
			else if(isPressed)
				skin.pressed
			else if(isHover)
				skin.hover
			else
				skin.normal;

		if(isEnabled) batch.color = tint;
		texture.draw(batch, x, y, width, height);
		batch.color = Color.WHITE;
		drawText(batch, font, caption, x, y + height * 0.5f + 6f, fontColor, 1.0f, width, Align.center);
	}

	/**
	 * 현재 프레임에서 바로 가기 키 처리
	 */
	private fun detectAccessKeyPress() {
		if(!isEnabled || !isVisible) return;
		if(accessKey != null && Input.isKeyJustPressed(accessKey.code - 36))
			onClick.run();
	}

	/**
	 * 단추를 누를 수 있게 한다.
	 */
	fun enable() {
		isEnabled = true;
	}

	/**
	 * 단추를 누를 수 없게 한다.
	 */
	fun disable() {
		isEnabled = false;
	}

	/**
	 * 단추 누르기
	 */
	private fun fireClickEvent() {
		if(!previouslyPressed) return;
		onClick.run();
		previouslyPressed = false;
	}

	override fun dispose() {
		font.dispose();
	}

	/**
	 * 버튼의 스킨이다.
	 *
	 * @property normal               기본 상태에서의 9-patch 텍스처
	 * @property hover                마우스를 올렸을 때의 9-patch 텍스처
	 * @property pressed              누르고 있는 동안의 9-patch 텍스처
	 * @property disabled             비활성화된 단추의 9-patch 텍스처
	 * @property captionColor         단추 글자 색
	 * @property hoverCaptionColor    마우스를 올렸을 때 단추 글자 색
	 * @property pressedCaptionColor  누르고 있을 때 단추 글자 색
	 * @property disabledCaptionColor 비활성화된 단추 글자 색
	 */
	data class Skin(@JvmField val normal: NinePatch, @JvmField val hover: NinePatch, @JvmField val pressed: NinePatch, @JvmField val disabled: NinePatch, @JvmField val captionColor: Color, @JvmField val hoverCaptionColor: Color, @JvmField val pressedCaptionColor: Color, @JvmField val disabledCaptionColor: Color) {
		constructor(normal: NinePatch) : this(normal, normal, normal, normal, DEFAULT_CAPTION_COLOR, DEFAULT_CAPTION_COLOR, DEFAULT_CAPTION_COLOR, DEFAULT_DISABLED_CAPTION_COLOR);

		constructor(normal: NinePatch, captionColor: Color) : this(normal, normal, normal, normal, captionColor, captionColor, captionColor, DEFAULT_DISABLED_CAPTION_COLOR);

		constructor(normal: NinePatch, captionColor: Color, hoverCaptionColor: Color, pressedCaptionColor: Color, disabledCaptionColor: Color) : this(normal, normal, normal, normal, captionColor, hoverCaptionColor, pressedCaptionColor, disabledCaptionColor);

		constructor(normal: NinePatch, hover: NinePatch, pressed: NinePatch, disabled: NinePatch) : this(normal, hover, pressed, disabled, DEFAULT_CAPTION_COLOR, DEFAULT_CAPTION_COLOR, DEFAULT_CAPTION_COLOR, DEFAULT_DISABLED_CAPTION_COLOR);

		constructor(normal: NinePatch, hover: NinePatch, pressed: NinePatch, disabled: NinePatch, captionColor: Color) : this(normal, hover, pressed, disabled, captionColor, captionColor, captionColor, DEFAULT_DISABLED_CAPTION_COLOR);
	}

	/**
	 * 단추 빌더 (자바 개발자 전용)
	 *
	 * @constructor 동적 크기를 사용하는 위젯
	 * @param x      X 좌표 계산 함수
	 * @param y      Y 좌표 계산 함수
	 * @param width  단추 너비 계산 함수
	 * @param height 단추 높이 계산 함수
	 */
	class Builder(private val x: FloatSupplier, private val y: FloatSupplier, private val width: FloatSupplier, private val height: FloatSupplier) {
		private var caption = "";
		private lateinit var buttonSkin: Skin;  // Overload resolution ambiguity between candidates 때문에 변수명 다르게
		private var color = Color.WHITE;
		private var clickHandler: Runnable = {};

		/**
		 * 단추 빌더 (자바 개발자 전용)
		 *
		 * @constructor 정적 크기를 사용하는 위젯
		 * @param x      X 좌표
		 * @param y      Y 좌표
		 * @param width  단추 너비
		 * @param height 단추 높이
		 */
		constructor(x: Float, y: Float, width: Float, height: Float) : this({ x }, { y }, { width }, { height });

		fun caption(caption: String): Builder {
			this.caption = caption;
			return this;
		}

		fun skin(skin: Skin): Builder {
			this.buttonSkin = skin;
			return this;
		}

		fun color(color: Color): Builder {
			this.color = color;
			return this;
		}

		fun onClick(handler: Runnable): Builder {
			this.clickHandler = handler;
			return this;
		}

		fun build(): Button {
			if(!::buttonSkin.isInitialized)
				throw IllegalStateException("skin is not set");
			return Button(x, y, width, height, caption, buttonSkin, color, clickHandler);
		}
	}
}
