package com.vk.challenge.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 09.09.17.
 */

public class ListUtils {

    public interface Map<T, P>{
        P map(T object);
    }

    public static <T, P> List<P> map(List<T> list, Map<T, P> map) {
        List<P> pList = new ArrayList<>(list.size());
        for (T t : list) {
            pList.add(map.map(t));
        }
        return pList;
    }
}
