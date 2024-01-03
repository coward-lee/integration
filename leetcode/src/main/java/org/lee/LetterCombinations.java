package org.lee;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LetterCombinations {

    public List<String> letterCombinations(String digits) {
        if (digits.length() == 0) {
            return List.of();
        }

        Map<Character, List<Character>> map = Map.of(
                '2', List.of('a', 'b', 'c'),
                '3', List.of('e', 'f', 'd'),
                '4', List.of('g', 'h', 'i'),
                '5', List.of('j', 'k', 'l'),
                '6', List.of('m', 'n', 'o'),
                '7', List.of('p', 'q', 'r', 's'),
                '8', List.of('t', 'u', 'v'),
                '9', List.of('w', 'x', 'y', 'z')
        );
        if (digits.length() == 1) {
            return map
                    .get(digits.toCharArray()[0])
                    .stream().map(String::valueOf)
                    .collect(Collectors.toList());
        }
        char[] charArray = digits.toCharArray();

        List<List<Character>> list = new LinkedList<>();
        for (char c : charArray) {
            List<Character> e = map.get(c);
            list.add(e);
        }

        return recursiveFetch("", list.size(),0, list);
    }

    private List<String> recursiveFetch(String startStr, int len, int index, List<List<Character>> list) {
        if (index == len) {
            ArrayList<String> objects = new ArrayList<>(1);
            objects.add(startStr);
            return objects;
        }
        return list.get(index).stream().flatMap(
                c -> recursiveFetch(startStr + c, len, index + 1, list).stream()
        ).collect(Collectors.toList());

    }

    public static void main(String[] args) {
        List<String> strings = new LetterCombinations().letterCombinations("23");
        strings.forEach(System.out::println);
    }
}
