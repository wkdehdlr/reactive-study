package com.example.demo;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class FluxScheduler {
    public static void main(String[] args) {
        Flux.range(1, 10)
                .publishOn(Schedulers.newSingle("pub"))
                .subscribeOn(Schedulers.newSingle("sub"))
                .subscribe(System.out::println);

        Flux.interval(Duration.ofMillis(500))
                .subscribe(System.out::println);
    }
}
