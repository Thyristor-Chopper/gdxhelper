package io.potatogun.gdxhelper.util;

/**
 * 자바에서만 사용할 수 있는 메쏘드/함수/필드/속성
 *
 * 코틀린에서 사용하려고 하면 오류가 난다.
 *
 * 굳이 꼭 우회해야 하면 `@OptIn(JavaOnly::class)`를 붙이면 된다.
 *
 * 만약 자동완성에도 나오지 않게 하고 우회도 어렵게 하고 
 *   코틀린 내에서는 완전히 존재하지 않는 유령 취급되게 하려면
 *   `@SinceKotlin("9999.9")`(`@Suppress("NEWER_VERSION_IN_SINCE_KOTLIN")`도
 *   필요할 수 있음) 하면 된다. 자바에서는 여전히 정상 접근 가능하며
 *   gdx-helper 기반으로 순수 자바로 간단한 게임을 직접 새로 만들어서 확인했다.
 */
@RequiresOptIn(
	level = RequiresOptIn.Level.ERROR,
	message = "this class, constructor, field, function or property is meant to be used in Java only"
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class JavaOnly;
