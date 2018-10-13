package stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by xubai on 2018/10/12 下午5:23.
 *
 * java8 流式操作
 * https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/index.html
 *
 * Intermediate：
 * map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
 *
 * Terminal：
 * forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
 *
 * Short-circuiting：
 * anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit
 *
 */
public class StreamTest {

    public static void main(String[] args) {
        StreamTest test = new StreamTest();
        //test.op();
        //test.convert();
        test.map();
    }

    public void build(){
        //indivial values
        Stream stream = Stream.of("a","b","c");
        //array
        String[] arraryStr = new String[]{"a","b","c"};
        stream = Stream.of(arraryStr);
        //collection
        List<String> stringList = Arrays.asList(arraryStr);
        stream = stringList.stream();
    }

    public void op(){
        IntStream.of(new int[]{1, 2, 3}).forEach(System.out::println);
        IntStream.range(1, 3).forEach(System.out::println);
        IntStream.rangeClosed(1, 6).forEach(System.out::println);
    }

    public void convert(){
        Stream<String> stream = Stream.of("a", "c", "d");
        //String[] strArray1 = stream.toArray(String[]::new);
        //Arrays.asList(strArray1).stream().forEach(System.out::println);
        //list
        //List<String> list1 = stream.collect(Collectors.toList());
        //list1.stream().forEach(System.out::println);
        List<String> list2 = stream.collect(Collectors.toCollection(ArrayList::new));
        list2.stream().forEach(System.out::println);
        //Set<String> set1 = stream.collect(Collectors.toSet());
        //Stack stack = stream.collect(Collectors.toCollection(Stack::new));
        String str = stream.collect(Collectors.joining());

    }

    /**
     *
     * map() 一对一
     * flatMap() 一对多:结构扁平化
     *
     *
     */
    public void map(){
        //List<String> wordList = Arrays.asList("得分", "新的", "预防");
        //List<String> output = wordList.stream().map(String::toUpperCase).collect(Collectors.toList());

        Stream<List<Integer>> inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );
        Stream<Integer> outputStream = inputStream.
                flatMap((childList) -> childList.stream());
    }

}
