package co.com.s4n.training.java;
import io.vavr.control.Option;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.BufferPoolMXBean;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vavr.API.None;

public class AverageCalculator {

    public static Option<String> leerArchivo(String fn){
        List<String> resultado;
        String resultadoFin = "";
        try(Stream<String> stream = Files.lines(Paths.get(fn))){
            System.out.println("************>>>>>>>>>>>>>>> Paso");

            resultado = stream
                    .collect(Collectors.toList());

            for (String res: resultado
                 ) {
                resultadoFin += res+";";
            }

            System.out.println(resultadoFin);
            return Option.of(resultadoFin);

        }catch (IOException e){
           return None();
        }
    }

    public static Option<String> calcularPromedio (String s){
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

        return Option.of(respuesta);

    }


    public static Option<String> verificarSiPaso(String s){
        return (Double.valueOf(s) >= 3)? Option.of("Paso"): Option.of("No paso");
    }


}
