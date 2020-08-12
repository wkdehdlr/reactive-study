package com.example.demo;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Operator {
    public static void main(String[] args) {

        Publisher<Integer> pub = getPub(Stream.iterate(1, a->a+1).limit(10).collect(Collectors.toList()));
        Publisher<Integer> mapPub = mapPub(pub, (Function<Integer, Integer>) s -> s * 10);
        Publisher<Integer> reducePub = reducePub(mapPub, 0, (BiFunction<Integer, Integer, Integer>) (a,b) -> a + b);
        reducePub.subscribe(sub());
    }
    private static Subscriber<Integer> sub() {
        return new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private static Publisher<Integer> reducePub(Publisher<Integer> mapPub, int init, BiFunction<Integer, Integer, Integer> bf) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                mapPub.subscribe(new Subscriber<Integer>() {
                    int result = init;
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        sub.onNext(bf.apply(result, integer));
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        };
    }


    private static Publisher<Integer> mapPub(Publisher<Integer> pub, Function<Integer, Integer> f) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                pub.subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        sub.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        sub.onNext(f.apply(integer));
                    }

                    @Override
                    public void onError(Throwable t) {
                        sub.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        sub.onComplete();
                    }
                });
            }
        };
    }


    private static Publisher<Integer> getPub(List<Integer> iter) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> sub) {
                sub.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try{
                            iter.forEach(s->sub.onNext(s));
                            sub.onComplete();
                        }catch (Throwable t){
                            sub.onError(t);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    }
}
