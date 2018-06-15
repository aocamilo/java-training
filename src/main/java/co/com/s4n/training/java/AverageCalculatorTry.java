package co.com.s4n.training.java;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.management.BufferPoolMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vavr.API.None;

public class AverageCalculatorTry {

    public static Try<Stream<String>> leerLineasArchivo(String fn){
        Try<Stream<String>> streamTry = Try.of(()-> Files.lines((Paths.get(fn))))
                .recover(Exception.class, Stream.of());
        System.out.println("()()()()) " + streamTry.isEmpty());
        return (!streamTry.isEmpty())? streamTry:Try.failure(new Exception("Archivo no existe"));

    }

    public static Try<String> concatenarArchivo(Stream<String> s){
        String resultadoFin ="";
        List<String> resultado;
        resultado = s
                .collect(Collectors.toList());

        for (String res: resultado
                ) {
            resultadoFin += res+";";
        }

        final String result = resultadoFin;

        return Try.of(() -> result);
    }

    public static Try<String> calcularPromedio (String s){
        String []valor = s.split(";");
        List<String> l = Arrays.asList(valor);

        System.out.println("********** Este es el valor de lo que ingresa -> "+ s);
        System.out.println("********** Este es el valor de lo que convierto -> "+ l);

        OptionalDouble promedio =
                l.stream()
                        .mapToDouble(x -> Double.parseDouble(x))
                        .average();

        String respuesta = String.valueOf(promedio.orElseGet(() -> 666));

        System.out.println("********** Este es el valor del promedio -> "+ respuesta);

        return Try.of(() -> respuesta);

    }


    public static Try<String> verificarSiPaso(String s){
        return (Double.valueOf(s) >= 3)? Try.of(()->"Paso"): Try.of(()->"No paso");
    }
}
