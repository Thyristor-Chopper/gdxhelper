@file:JvmName("ArgumentParser")
package io.potatogun.gdxhelper.util;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

import io.potatogun.gdxhelper.collections.CharMap;
import io.potatogun.gdxhelper.collections.get;
import io.potatogun.gdxhelper.collections.toCharMap;

/**
 * 풀그림 매개변수를 파싱하여 맵으로 반환한다.
 *
 * @param args 매개변수 배열
 */
fun parseArguments(args: Array<String>): ObjectMap<String, String> = parseArguments(args, null, null);

/**
 * 풀그림 매개변수를 파싱하여 맵으로 반환한다.
 *
 * @param args             매개변수 배열
 * @param shortArgumentMap 짧은 매개변수(작대기 한 개) 별칭 맵 - 자바에서 키로 문자 자체를 사용한다.
 */
@SinceKotlin("9999.9") fun parseArguments(args: Array<String>, shortArgumentMap: ObjectMap<Char, String>): ObjectMap<String, String> = parseArguments(args, shortArgumentMap, null);

/**
 * 풀그림 매개변수를 파싱하여 맵으로 반환한다.
 *
 * @param args             매개변수 배열
 * @param shortArgumentMap 짧은 매개변수(작대기 한 개) 별칭 맵 - 자바에서 키로 문자 자체를 사용한다.
 */
@SinceKotlin("9999.9") fun parseArguments(args: Array<String>, shortArgumentMap: ObjectMap<Char, String>, valueRequired: ObjectSet<String>?): ObjectMap<String, String> = parseArguments(args, shortArgumentMap.toCharMap(), valueRequired);

/**
 * 풀그림 매개변수를 파싱하여 맵으로 반환한다.
 *
 * 중복된 매개변수는 마지막 항목이 반영된다.
 *
 * 구현의 귀찮음으로 인해
 *   리눅스와는 다르게 -abc 3라고 전달하면 -a, -b, -c에 모두 3이 할당되고
 *   -abc=3이라고 쓰면 'a', 'b', 'c', '=', '3'을 각각 별칭으로 취급한다.
 *
 * @param args             매개변수 배열
 * @param shortArgumentMap 짧은 매개변수(작대기 한 개) 별칭 맵 - 자바에서는 키로 문자 코드를 사용한다.
 * @param valueRequired    값이 반드시 요구되는 매개변수 키 목록. --option --abcd라고 입력하면 다음 
 */
fun parseArguments(args: Array<String>, shortArgumentMap: CharMap<String>? = null, valueRequired: ObjectSet<String>? = null): ObjectMap<String, String> {
	val length = args.size;
	val options = ObjectMap<String, String>(length);
	for(i in 0 until length) {  // 인덱스를 직접 써야 할 일이 있어서 for..in 안 씀
		val rawArgument = args[i];
		if(rawArgument.startsWith("--")) {
			parseArgument(rawArgument.removePrefix("--"), args, length, i, options);
		} else if(shortArgumentMap != null && rawArgument.startsWith("-")) {
			val argumentCount = rawArgument.length - 1;
			for(k in 1..argumentCount)
				shortArgumentMap.get(rawArgument[k])?.let { parseArgument(it, args, length, i, options) };
		}
	}
	return options;
}

private inline fun parseArgument(argument: String, args: Array<String>, length: Int, i: Int, options: ObjectMap<String, String>) {
	val key: String;
	val value: String;
	val equalsIndex = argument.indexOf('=');
	if(equalsIndex != -1) {
		key = argument.substring(0, equalsIndex);
		value = argument.substring(equalsIndex + 1);
	} else {
		key = argument;
		value = if(length > i + 1) args[i + 1].takeIf { !it.startsWith("-") } ?: "" else "";
	}
	options.put(key, value);
}
