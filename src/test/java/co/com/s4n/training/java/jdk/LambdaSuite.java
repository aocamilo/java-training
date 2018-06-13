package co.com.s4n.training.java.jdk;

import static org.junit.Assert.*;

import io.vavr.collection.List;
import org.junit.Test;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;

public class LambdaSuite {

        @FunctionalInterface
        interface InterfaceDeEjemplo{
            int metodoDeEjemplo(int x, int y);
        }

        class ClaseDeEjemplo{
            public int metodoDeEjemplo1(int z, InterfaceDeEjemplo i){
                return z + i.metodoDeEjemplo(1,2);
            }

            public int metodoDeEjemplo2(int z, BiFunction<Integer, Integer, Integer> fn){
                return z + fn.apply(1,2);
            }
        }

        @Test
        public void smokeTest() {
            assertTrue(true);
        }

        @Test
        public void usarUnaInterfaceFuncional1(){

            InterfaceDeEjemplo i = (x,y)->x+y;

            ClaseDeEjemplo instancia = new ClaseDeEjemplo();

            int resultado = instancia.metodoDeEjemplo1(1,i);

            assertTrue(resultado==4);
        }

        @Test
        public void usarUnaInterfaceFuncionalDuplicada(){

            InterfaceDeEjemplo j = (x,y)->(2*x)+(2*y);

            ClaseDeEjemplo instancia = new ClaseDeEjemplo();

            int resultado = instancia.metodoDeEjemplo1(1,j);

            assertTrue(resultado==7);
        }

        @Test
        public void usarUnaInterfaceFuncional2(){

            BiFunction<Integer, Integer, Integer> f = (x, y) -> new Integer(x.intValue()+y.intValue());

            ClaseDeEjemplo instancia = new ClaseDeEjemplo();

            int resultado = instancia.metodoDeEjemplo2(1,f);

            assertTrue(resultado==4);
        }

        @Test
        public void usarUnaInterfaceFuncional2Duplicada(){

            BiFunction<Integer, Integer, Integer> f = (x, y) -> new Integer((2*x.intValue())/y.intValue());

            ClaseDeEjemplo instancia = new ClaseDeEjemplo();

            int resultado = instancia.metodoDeEjemplo2(1,f);

            assertTrue(resultado==2);
        }

        class ClaseDeEjemplo2{

            public int metodoDeEjemplo2(int x, int y, IntBinaryOperator fn){
                return fn.applyAsInt(x,y);
            }
        }
        @Test
        public void usarUnaFuncionConTiposPrimitivos(){
            IntBinaryOperator f = (x, y) -> x + y;

            ClaseDeEjemplo2 instancia = new ClaseDeEjemplo2();

            int resultado = instancia.metodoDeEjemplo2(1,2,f);

            assertEquals(3,resultado);
        }

        @Test
        public void usarUnaFuncionConTiposPrimitivosDuplicado(){
            IntBinaryOperator f = (x, y) -> x + y;

            ClaseDeEjemplo2 instancia = new ClaseDeEjemplo2();

            int resultado = instancia.metodoDeEjemplo2(1,2,f);

            assertEquals(3,resultado);
        }

        class ClaseDeEjemplo3{

            public String operarConSupplier(Supplier<Integer> s){
                return "El int que me han entregado es: " + s.get();
            }
        }

        @Test
        public void usarUnaFuncionConSupplier(){
            Supplier s1 = () -> {
                System.out.println("Cuándo se evalúa esto? (1)");
                return 4;
            };

            Supplier s2 = () -> {
                System.out.println("Cuándo se evalúa esto? (2)");
                return 4;
            };

            ClaseDeEjemplo3 instancia = new ClaseDeEjemplo3();

            String resultado = instancia.operarConSupplier(s2);

            assertEquals("El int que me han entregado es: 4",resultado);
        }

        class ClaseDeEjemplo4{

            private int i = 0;

            public void operarConConsumer(Consumer<Integer> c){
                c.accept(i);
            }
        }

        @Test
        public void usarUnaFuncionConConsumer(){
            Consumer<Integer> c1 = x -> {
                System.out.println("Me han entregado este valor: "+x);
            };

            ClaseDeEjemplo4 instancia = new ClaseDeEjemplo4();

            instancia.operarConConsumer(c1);

        }

        class ClaseDeEjemplo5{

            public void operarConConsumer2(Consumer<Integer> c, Integer i){
                c.accept(i);
            }
        }

        @Test
        public void usarUnaFuncionConConsumer2(){
            Consumer<Integer> c1 = x -> {
                System.out.println("Me han entregado este valor: "+x);
            };

            ClaseDeEjemplo5 instancia = new ClaseDeEjemplo5();

            instancia.operarConConsumer2(c1, 9);

        }

        @FunctionalInterface
        interface InterfaceDeEjercicio{
            Consumer<Integer> suppliersAConsumer(Supplier<Integer> s1, Supplier<Integer> s2, Supplier<Integer> s3);
        }

        @Test
        public void ejecutarOperaciones(){
            InterfaceDeEjercicio i = (s1, s2, s3)->{
                Consumer<Integer> c = n->{
                    Integer suma = s1.get() + s2.get() + s3.get() + n;
                    System.out.println("La Suma es: " + suma);
                };
                return c;
            };

            Supplier s1= ()-> 4;

            Supplier s2= ()-> 5;

            Supplier s3= ()-> 6;

            Consumer<Integer> c = i.suppliersAConsumer(s1,s2,s3);

            c.accept(new Integer(5));
        }

        @FunctionalInterface
        interface interfaceDePractica{
            String soyUnLambda(Supplier<Integer> x);
        }

        @Test
        public void t(){
            interfaceDePractica i = x -> (x.get().equals(new Integer(1)))?"Me dicen que si": "Me dicen que no";
            Supplier x= ()-> 1;
            String resultado = i.soyUnLambda(x);
            System.out.println("----------------->>"+ resultado);
            assertEquals("Me dicen que si", resultado);
        }

}
