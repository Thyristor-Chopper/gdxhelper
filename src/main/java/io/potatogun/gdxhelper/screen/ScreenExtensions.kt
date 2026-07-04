@file:JvmName("ScreenExtensions")
package io.potatogun.gdxhelper.screen;

import io.potatogun.gdxhelper.widget.Widget;

/**
 * 위젯을 식별자로 가져온다. 없으면 null이다.
 *
 * @param id 가져올 위젯의 식별자
 * @return 해당 위젯(없으면 null)
 */
inline fun Screen.getWidgetOrNull(id: String): Widget? {
	return try {
		getWidget(id)
	} catch(e: NoSuchElementException) {
		null
	};
}
