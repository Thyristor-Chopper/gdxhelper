package io.potatogun.gdxhelper.util;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import io.potatogun.gdxhelper.collections.WeakMutableSet;

/**
 * 공유 자원(텍스처 또는 오디오) 관리자
 */
abstract class SharedResourceManager<T : Disposable> @JvmOverloads constructor(private val pathPrefix: String = "") : Disposable {
	private val shared = ObjectMap<String, Lazy<T>>();

	/**
	 * 지정한 경로의 화일에서 자원을 등록한다.
	 *
	 * 이 라우트는 텍스처와 오디오만 지원한다. 이 클래스를 생성했을 때 제네릭으로 Texture 또는 Sound을 넘겨야 하며 그렇지 않으면 런타임 오류가 날 것이다.
	 *
	 * @param id        식별자
	 * @param path      자원 경로
	 * @param immediate 즉시 불러올지의 여부
	 * @throws ClassCastException 제네릭이 Texture 또는 Sound가 아닌데 이 메쏘드로 자원을 등록하려는 경우
	 */
	@JvmOverloads protected fun register(id: String, path: String, immediate: Boolean = false) {
		if(immediate) {
			val resource = (if(isSound(path)) loadSound(pathPrefix + path) else loadTexture(pathPrefix + path)) as T;
			sharedResources.add(resource);
			shared.put(id, lazyOf(resource));
		} else {
			shared.put(id, lazy {
				val resource = (if(isSound(path)) loadSound(pathPrefix + path) else loadTexture(pathPrefix + path)) as T;
				sharedResources.add(resource);

				/* return */ resource
			});
		}
	}

	/**
	 * 텍스처를 등록한다.
	 *
	 * @param id       식별자
	 * @param resource 자원
	 */
	protected fun register(id: String, resource: T) {
		sharedResources.add(resource);
		shared.put(id, lazyOf(resource));
	}

	/**
	 * 지정한 이름의 공유 텍스처를 가져온다.
	 *
	 * @param id 텍스처 식별자
	 * @return 지정한 식별자의 텍스처
	 * @throws NoSuchElementException 지정한 식별자의 텍스처가 없는 경우
	 */
	fun getShared(id: String): T = shared[id]?.value ?: throw NoSuchElementException("invalid shared resource ID");

	/**
	 * 지정한 이름의 공유 텍스처가 있는지 확인한다.
	 *
	 * @param id 텍스처 식별자
	 * @return 존재 여부
	 */
	fun hasShared(id: String): Boolean = shared.containsKey(id);

	/**
	 * 공유 텍스처(여기서 정의된 텍스처)를 dispose한다.
	 *
	 * Screen이나 World가 아닌 Game에서 호출해야 한다.
	 */
	override fun dispose() {
		val iterator = shared.values();
		while(iterator.hasNext) {
			val resource = iterator.next();
			if(resource.isInitialized())
				resource.value.dispose();
		}
	}

	companion object {
		private val sharedResources = WeakMutableSet<Disposable>();

		/**
		 * 공유 자원인지 확인한다.
		 *
		 * @param resource 확인할 자원
		 * @return 공유 자원 여부
		 */
		@JvmStatic fun isShared(resource: Disposable): Boolean = sharedResources.contains(resource);

		private inline fun isSound(path: String): Boolean {
			val length = path.length;
			return length >= 4 && path[length - 4] == '.' && (
				((path[length - 3] == 'm' || path[length - 3] == 'M') && (path[length - 2] == 'p' || path[length - 2] == 'P') && (path[length - 1] == '3')) ||
				((path[length - 3] == 'o' || path[length - 3] == 'O') && (path[length - 2] == 'g' || path[length - 2] == 'G') && (path[length - 1] == 'g' || path[length - 1] == 'G')) ||
				((path[length - 3] == 'w' || path[length - 3] == 'W') && (path[length - 2] == 'a' || path[length - 2] == 'A') && (path[length - 1] == 'v' || path[length - 1] == 'V'))
			);
		}
	}
}
