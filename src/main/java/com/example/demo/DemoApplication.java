package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		try{
            zip();
//            concat();
//            merge();
//            concatmap();
//            flatmap();
//			blockfirst();
		}catch (Exception e){
			System.out.println("e = " + e);
		}
	}

	public static void concat() throws InterruptedException {
		Flux.concat(
				Flux.interval(Duration.ofMillis(100))
						.take(4)
						.map(value -> value * 10),
				Flux.interval(Duration.ofMillis(10))
						.take(4)
		).subscribe(e -> System.out.println("e = " + e));

		Thread.sleep(500);

	}

	public static void merge() throws InterruptedException {
		Flux.merge(
				Flux.interval(Duration.ofMillis(100))
						.take(4)
						.map(value -> value * 10),
				Flux.interval(Duration.ofMillis(10))
						.take(4)
		).subscribe(e -> System.out.println("e = " + e));

		Thread.sleep(500);
	}

	public static void zip() throws InterruptedException {
		Flux.zip(
				Flux.interval(Duration.ofMillis(100))
						.take(4)
						.map(value -> value * 10),
				Flux.interval(Duration.ofMillis(10))
						.take(4)
		).subscribe(e -> System.out.println("e = " + e));

		Thread.sleep(500);
	}

	public static void flatmap() throws InterruptedException {

		Flux.range(1, 2)
				.flatMap(av -> Flux.interval(Duration.ofMillis(100))
						.take(5))
				.doOnNext(v -> System.out.print("doOnNext :: " + v))
				.subscribe(System.out::println);

		Thread.sleep(500);

	}

	public static void concatmap() throws InterruptedException {

		Flux.range(1, 2)
				.concatMap(av -> Flux.interval(Duration.ofMillis(100))
						.take(5))
				.doOnNext(v -> System.out.print("doOnNext :: " + v))
				.subscribe(System.out::println);

		Thread.sleep(500);

	}

	public static void blockfirst() throws InterruptedException{
		Flux<Long> just = Flux.interval(Duration.ofMillis(1000));
		Long aLong = just.blockFirst(Duration.ofMillis(2000));
		System.out.println("aLong = " + aLong);
	}

}
