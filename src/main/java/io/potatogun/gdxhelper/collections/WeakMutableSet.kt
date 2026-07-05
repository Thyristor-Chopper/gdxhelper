package io.potatogun.gdxhelper.collections;

import java.util.WeakHashMap;

/**
 * 약한 참조를 갖는 집합
 * 
 * 강한 참조가 모두 사라지면 집합에서 자동으로 없어진다.
 *
 * 자바의 WeakHashMap의 키를 활용하여 구현되었다.
 */
class WeakMutableSet<T> : MapKeyMutableSet<T>(WeakHashMap<T, Nothing?>());
