package io.potatogun.gdxhelper;

import com.badlogic.gdx.Game as GdxGame;
import com.badlogic.gdx.Screen as GdxScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

import io.potatogun.gdxhelper.Window;
import io.potatogun.gdxhelper.screen.Screen;
import io.potatogun.gdxhelper.world.World;

/**
 * 이 프레임워크에 맞게 일부 처리가 추가된 Game 추상 클래스
 */
abstract class Game : GdxGame() {
	// Gdx.graphics.width를 매번 실수형으로 변환하는 오버헤드를 없애기 위해 창 크기를 캐시하고 크기가 바뀔 때만 업데이트한다.
	override fun resize(width: Int, height: Int) {
		// 창 크기 캐시
		Window.updateWindowDimensions();

		// 화면에 크기 조절 이벤트 발생
		val screen = getScreen();
		if(screen is Screen) {
			screen.updateProjectionMatrix();
			screen.resize(width, height);
		} else {
			super.resize(width, height);
		}
	}

	/**
	 * 매 프레임 상태를 갱신한다.
	 *
	 * @param delta 직전 프레임과의 시간 간격(초)
	 */
	open fun update(delta: Float) {}

	override fun render() {
		// 상태 갱신
		val delta = Gdx.graphics.getDeltaTime();
		update(delta);

		// 스크린 그리기
		val screen = getScreen();
		if(screen is Screen) {
			screen.updateAll(delta);
			screen.render();
		} else {
			super.render();
		}
	}

	override fun setScreen(screen: GdxScreen) {
		super.setScreen(screen);

		// 다른 화면에서 크기를 조절했다가 다시 복귀했을 때 대비
		if(screen is Screen) {
			screen.updateProjectionMatrix();
			screen.resize(Window.intWidth, Window.intHeight);
		}
	}
}
