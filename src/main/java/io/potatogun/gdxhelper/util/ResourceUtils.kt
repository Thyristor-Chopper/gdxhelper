@file:JvmName("ResourceUtils")
package io.potatogun.gdxhelper.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * 지정한 화일 이름의 텍스처를 가져온다.
 *
 * @param path 화일 이름 또는 경로
 * @return 불러온 텍스처 객체
 */
inline fun loadTexture(path: String): Texture = Texture(Gdx.files.internal(path));

/**
 * 지정한 화일 이름의 음악을 가져온다.
 *
 * @param path 화일 이름 또는 경로
 * @return 불러온 오디오 객체
 */
inline fun loadMusic(path: String): Music = Gdx.audio.newMusic(Gdx.files.internal(path));

/**
 * 지정한 화일 이름의 오디오를 가져온다.
 *
 * @param path 화일 이름 또는 경로
 * @return 불러온 오디오 객체
 */
inline fun loadSound(path: String): Sound = Gdx.audio.newSound(Gdx.files.internal(path));

/**
 * 공유 자원이 아닌 경우에만 비디오 카드 메모리에서 정리한다.
 *
 * @param resource 해제할 자원
 * @return 성공 여부
 */
inline fun safeDispose(resource: Disposable): Boolean {
	if(SharedResourceManager.isShared(resource)) return false;
	resource.dispose();
	return true;
}
