package reactive;

import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * <pre>
 *
 *  File: MonoTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/5/28				lijunjun				Initial.
 *
 * </pre>
 */
public class MonoTest {

    public static void main(String[] args) {
        Mono.fromSupplier(() -> "hello").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("hellow")).subscribe(System.out::println);
        Mono.create(monoSink -> monoSink.success("Hello")).subscribe(System.out::println);

    }

}
