@file:JvmName("ArgumentParser")
package io.potatogun.gdxhelper.util;

import com.badlogic.gdx.utils.ObjectMap;

import io.potatogun.gdxhelper.collections.CharMap;
import io.potatogun.gdxhelper.collections.get;

/**
 * 풀그림 매개변수를 파싱하여 맵으로 반환한다.
 *
 * 중복된 매개변수는 마지막 항목이 반영된다.
 *
 * @param args             매개변수 배열
 * @param shortArgumentMap 짧은 매개변수(작대기 한 개) 별칭 맵
 */
@JvmOverloads fun parseArguments(args: Array<String>, shortArgumentMap: CharMap<String>? = null): ObjectMap<String, String> {
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
