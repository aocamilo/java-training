package co.com.s4n.training.java.vavr;

import io.vavr.Lazy;
import io.vavr.concurrent.Future;

import java.util.function.Supplier;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class LazySuite {
    @Test
    public void testWithTime(){
        System.out.println("-> Prueba strict");
        Future<Integer> f1 = Future.of(() -> {
            sleep(500);
            return 1;
        });
        Future<Integer> f2 = Future.of(() -> {
            sleep(800);
            return 1;
        });
        Future<Integer> f3 = Future.of(() -> {
            sleep(300);
            return 1;
        });

        long inicio = System.nanoTime();

        Future<Integer> resultado = f1
                .flatMap(a -> f2
                        .flatMap(b -> f3
                                .flatMap(c -> Future.of(()-> a+b+c))));

        resultado.await();
        long fin = System.nanoTime();

        long elapsed = (fin - inicio);

        System.out.println(elapsed* Math.pow(10, -6));
        System.out.println(resultado.getOrElse(666));

        System.out.println("-> Fin Prueba strict");

    }

    @Test
    public void testWithLazyTime(){
        System.out.println("-> Prueba Lazy");
        Lazy<Future<Integer>> f1 = Lazy.of(() -> Future.of(() -> {
            sleep(500);
            return 1;
        }));
        Lazy<Future<Integer>> f2 = Lazy.of(() -> Future.of(() -> {
            sleep(800);
            return 1;
        }));
        Lazy<Future<Integer>> f3 = Lazy.of(() -> Future.of(() -> {
            sleep(300);
            return 1;
        }));

        long inicio = System.nanoTime();

        Future<Integer> res =
                f1.get()
                        .flatMap(a -> f2.get()
                                .flatMap(b -> f3.get()
                                        .flatMap(c -> Future.of(() -> a + b + c))));

        res.await();

        long fin = System.nanoTime();

        long elapsed = (fin - inicio);

        System.out.println(elapsed* Math.pow(10, -6));
        System.out.println(res.getOrElse(666));

        System.out.println("-> Fin Prueba Lazy");

    }

    @Test
    public void testWithLazyTimeWithCache(){
        System.out.println("-> Prueba Lazy Cache");
        Lazy<Future<Integer>> f1 = Lazy.of(() -> Future.of(() -> {
            sleep(500);
            return 1;
        }));
        Lazy<Future<Integer>> f2 = Lazy.of(() -> Future.of(() -> {
            sleep(800);
            return 1;
        }));
        Lazy<Future<Integer>> f3 = Lazy.of(() -> Future.of(() -> {
            sleep(300);
            return 1;
        }));

        long inicio1 = System.nanoTime();

        Future<Integer> res =
                f1.get()
                        .flatMap(a -> f2.get()
                                .flatMap(b -> f3.get()
                                        .flatMap(c -> Future.of(() -> a + b + c))));
        res.await();
        long fin1 = System.nanoTime();

        long inicio2 = System.nanoTime();
        Future<Integer> res2 =
                f1.get()
                        .flatMap(a -> f2.get()
                                .flatMap(b -> f3.get()
                                        .flatMap(c -> Future.of(() -> a + b + c))));

        res2.await();
        long fin2 = System.nanoTime();

        long elapsed1 = (fin1 - inicio1);
        long elapsed2 = (fin2 - inicio2);

        System.out.println("Me demore: "+elapsed1* Math.pow(10, -6)+" en la primera vez");
        System.out.println("Me demore: "+elapsed2* Math.pow(10, -6)+" en la segunda vez");
        System.out.println("Resultado 1: " + res.getOrElse(666));
        System.out.println("Resultado 2: " +res2.getOrElse(666));
        System.out.println("-> Fin Prueba Lazy Cache");

    }

    @Test
    public void supplierVsLazy(){
        Supplier s1 = ()->{
            try{
                sleep(500);
                return 1;
            }catch (Exception e){
                return 0;
            }

        };

        Lazy<Future<Integer>> f1 = Lazy.of(() -> Future.of(() -> {
            sleep(500);
            return 1;
        }));

        long inicio1 = System.nanoTime();
        Future<Integer> res = f1.get();
        res.await();
        long fin1 = System.nanoTime();

        long inicio2 = System.nanoTime();
        Future<Integer> res2 = f1.get();
        res2.await();
        long fin2 = System.nanoTime();

        double timeLazy1= ((fin1-inicio1)*Math.pow(10, -6));
        double timeLazy2= ((fin2-inicio2)*Math.pow(10, -6));

        long inicio3 = System.nanoTime();
        s1.get();
        long fin3 = System.nanoTime();

        long inicio4 = System.nanoTime();
        s1.get();
        long fin4 = System.nanoTime();

        double timeSupp1= ((fin3-inicio3)*Math.pow(10, -6));
        double timeSupp2= ((fin4-inicio4)*Math.pow(10, -6));

        System.out.println("Primer llamado Lazy :" + timeLazy1);
        System.out.println("Segundo llamado Lazy :" + timeLazy2);
        System.out.println("Primer llamado Supplier :" + timeSupp1);
        System.out.println("Segundo llamado Supplier :" + timeSupp2);


    }
}
