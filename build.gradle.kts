plugins {
	kotlin("jvm") version "2.4.0"
	id("org.jetbrains.dokka") version "1.9.0"  // 최신 버전 2.2.0은 힙을 2기가나 쳐먹으려고 하는데 32비트 OS에서 2기가 이상 할당 불가하고 아무도 최대 힙크기를 제대로 제한하는 방법을 안 알려주고 생성형 AI들은 다 작동도 하지 않는 이상한 세팅 알려주고...
}

dependencies {
	api("com.badlogicgames.gdx:gdx:1.14.2")
}

repositories {
	gradlePluginPortal()
}

tasks.withType<JavaCompile>().configureEach {
	// 자바 IDE가 함수를 자동완성할 때 매개변수명 보존
	options.compilerArgs.add("-parameters")

	// package-info.java 한글 깨짐 방지
	options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions {
		// 자바 IDE가 함수를 자동완성할 때 매개변수명 보존
		javaParameters.set(true)

		// Windows XP 호환성
		jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)

		// 일부 최적화
		freeCompilerArgs.addAll(listOf("-Xlambdas=indy", "-Xstring-concat=indy", "-Xno-call-assertions", "-Xno-receiver-assertions", "-Xno-source-debug-extension"))
		freeCompilerArgs.addAll(listOf("-Xwarning-level=NOTHING_TO_INLINE:disabled", "-Xwarning-level=UNCHECKED_CAST:disabled"))

		// 인터페이스 최적화
		jvmDefault = org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode.NO_COMPATIBILITY
	}
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
	dokkaSourceSets.configureEach {
		noJdkLink.set(true)
	}
}
