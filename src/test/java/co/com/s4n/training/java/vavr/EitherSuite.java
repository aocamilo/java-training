package co.com.s4n.training.java.vavr;

import co.com.s4n.training.java.biPeek;
import io.vavr.Function1;
import io.vavr.control.Either;
import org.junit.Test;

import static io.vavr.API.Left;
import static io.vavr.API.None;
import static io.vavr.API.Right;

import java.util.function.Consumer;
import java.io.Serializable;
import static org.junit.Assert.assertArrayEquals;
import io.vavr.control.Option;
import io.vavr.control.Try;

import javax.swing.text.ElementIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static io.vavr.API.*;
import static io.vavr.Patterns.$Left;
import static io.vavr.Patterns.$Right;
import static org.junit.Assert.*;

public class EitherSuite {

    /**
     * Se valida la funcion swap con un Either right y left en la cual se aplica swap para el cambio de tipo Either
     */
    @Test
    public void swapToEither() {
        Either<Integer,String> myEitherR = Either.right("String");
        Either<Integer,String> myEitherL = Either.left(14);
        assertTrue("Valide swap before in Either Right", myEitherR.isRight());
        assertFalse(myEitherR.isLeft());
        assertTrue("Valide swap after in Either Right", myEitherR.swap().isLeft());
        assertFalse(myEitherR.swap().isRight());
        assertTrue("Valide swap before in Either Left", myEitherL.isLeft());
        assertFalse(myEitherL.isRight());
        assertTrue("Valide swap after in Either Right", myEitherL.swap().isRight());
        assertFalse(myEitherL.swap().isLeft());
    }

    /**
     * Los Either se definen porque tienen un manejo por convencion derecha el valor correcto e
     * izquierda si fue incorrecto, se usan las projecciones para saber con cual lado se operará.
     * Es decir si los Either son correctos es porque tienen una valor en la derecha y se sumara un valor x,
     * sino el lado izquierdo sera un mensaje que diga either incorrecto.
     */
    @Test
    public void testProjection(){
        Either<Integer,Integer> e1 = Either.right(5);
        Either<Integer,Integer> e2 = Either.left(5);

        //El Either por defecto cuando se usa el map opera con el lado derecho.
        assertEquals("Failure - Right projection", Right(10), e1.map(it -> it + 5));

        //El Either para operar el lado izquierdo se debe usar un mapLeft.
        assertEquals("Failure - Left Projection", Left(10), e2.mapLeft(it -> it + 5));
    }

    @Test
    public void testProjectionMap(){
        Either<Integer,Integer> e1 = Either.right(5);
        Either<Integer,Integer> e2 = Either.left(5);

        //El Either por defecto cuando se usa el map opera con el lado derecho.
        assertEquals("Failure - Right projection", Right(10), e1.map(it -> it + 5));

        //El Either para operar el lado izquierdo se debe usar un mapLeft. Si no se usa el mapLeft, no se aplica la lambda.
        assertEquals("Failure - Left Projection", Left(5), e2.map(it -> it + 5));
    }

    /**
     * El map por defecto operara el lado derecho, si el either tiene su informacion en el lado izquierdo
     * este no se modificara
     */
    @Test
    public void testEitherMap() {
        Either<String,Double> value = Either.right( 2.0 / 3);

        assertEquals("Failure - Map in Right",
                Right(4.0),
                value.map(aDouble -> aDouble * 6));

        Either<String,Double> value2 = Either.left("Left side");

        assertEquals("Failure - the Either is not left",
                Left("Left side"),
                value2.map(aDouble -> aDouble * 6));

    }

    /**
     * El flatmap por defecto operara el lado derecho, si el either tiene su informacion en el lado izquierdo
     * este no se modificara
     */
    @Test
    public void testEitherFlatMap() {

        Either<String,Double> e1 = Either.right( 2.0 / 3);

        assertEquals("Failure - flatMap in Right",
                Right(4.0),
                e1.flatMap(aDouble -> Right(aDouble * 6)));

        Either<String,Double> e2 = Either.left("Left side");

        assertEquals("Failure - the Either is not left",
                Left("Left side"),
                e2.flatMap(aDouble -> Right(aDouble * 6)));

    }

    //programa ejemplo con either

    public Either<Integer, Integer> sum(int a, int b){
        return Either.right(a + b);
    }

    public Either<Integer, Integer> divide(int a, int b){
        Try<Integer> res = Try.of(()-> a/b);
        return (res.isSuccess())? Either.right(a/b): Either.left(0);
    }

    @Test
    public void myTestWithEither(){
        Either<Integer, Integer> res1 = sum(2, 2)
                .flatMap(r1 -> sum(r1, 2)
                        .flatMap(r2 -> divide(r2, 0)
                                .flatMap(r3 -> sum(r3, 2))));

        Either<Integer, Integer> res2 = sum(2, 2)
                .flatMap(r1 -> sum(r1, 2)
                        .flatMap(r2 -> sum(r2, 2)
                                .flatMap(r3 -> divide(r3, 2))));

        assertEquals(res1, Either.left(0));
        System.out.println("->> res1: "+ res1 );
        assertEquals(res2, Either.right(4));
        System.out.println("->> res2: "+ res2);
    }

    /**
     * Un Either puede ser filtrado, y en el predicado se pone la condicion
     */
    @Test
    public void testEitherFilter() {

        Either<String,Integer> value = Either.right(7);

        assertEquals("value is even",
                None(),
                value.filter(it -> it % 2 == 0));
    }

    @Test
    public void testEitherFilterSuccess() {

        Either<String,Integer> value = Either.right(8);

        assertEquals(
                Some(Either.right(8)),
                value.filter(it -> it / 2 == 4));
    }

    /**
     * Si el predicado del filter tiene un null, el void lanzara un Nullpointerexception
     */
    @Test(expected = NullPointerException.class)
    public void testEitherFilter2() {
        Either<String,Integer> value = Either.right(7);
        value.filter(null);
    }

    /**
     * La funcion bimap me permite realizar map a los Either en su derecha o izquierda
     * esto dependera de en cual lado tiene informacion
     * si es derecha aplica derecha
     * si es izquiera aplica izquierda
     */
    @Test
    public void testEitherBiMap() {
        Function1<String,String> left = (Function1<String , String>) string -> "this the left";
        Function1<Integer,Integer> right = (Function1<Integer,Integer>) integer -> integer + 15;

        Function1<Either,Either> biMap = (Function1<Either, Either>) either ->
                either.bimap(left,right);

        Either<String,Integer> value = Either.right(5);
        Either<String,Integer> value2 = Either.left("this is some");

        assertEquals("Failure map in right", Either.right(20),biMap.apply(value));
        assertEquals("Failure map in left", Either.left("this the left"),biMap.apply(value2));
    }




    @Test
    public void biPeekTest(){
        Either<String, String> left = Either.left("This the left");
        Either<String, String> right = Either.right("This the right");
        final String[] s = {""};
        co.com.s4n.training.java.biPeek bp = new biPeek();
        Consumer<String> c1 = element -> {
            s[0] += element;
        };

        Consumer<String> c2 = element -> {
            s[0] += element;
        };

        bp.biPeek2(left, c1, c2);
        assertEquals(s[0], "This the left");
        s[0]= "";
        bp.biPeek2(right, c1, c2);
        assertEquals(s[0], "This the right");

    }

    /**
     * Se valida la funcion orElseRun, la cual ejecuta una accion
     * si el either es either.left.
     */
    @Test
    public void testOrElseRun() {
        Either<Integer,String> myEitherR = Either.right("String");
        Either<Integer,String> myEitherL = Either.left(14);
        final String[] result = {"let's dance! "};
        Consumer<Object> addIfTrue = element -> {
            result[0] += element;
        };
        myEitherR.orElseRun(addIfTrue);
        assertEquals("Valide swap before in Either Right",
                "let's dance! ", result[0]);
        myEitherL.orElseRun(addIfTrue);
        assertEquals("Valide swap before in Either Right",
                "let's dance! 14", result[0]);
    }

    /**
     * Se valida la funcion peek y peekleft segun la proyeccion del Either ya sea right o left
     * peek solo ejecuta si es right, en caso de que se quiera usar peek sobre un left para que se ejecute
     * se debe usar peekLeft
     */
    @Test
    public void peekToEither() {

        final String[] valor = {"default"};
        Either<String,String> myEitherR = Either.right("123456");
        Either<String,String> myEitherL = Either.left("1234567");

        Consumer<String> myConsumer = element -> {
            if(element.length()>6){
                valor[0] = "foo";
            }
            else {
                valor[0] = "bar";
            }
        };

        myEitherL.peek(myConsumer);
        assertEquals("Validete Either with peek","default", valor[0]);

        myEitherR.peek(myConsumer);
        assertEquals("Validete Either with peek","bar", valor[0]);

        myEitherL.peekLeft(myConsumer);
        assertEquals("Validete Either with peek","foo", valor[0]);
    }

    //either, lambda y si es izq le hace peekleft y si es derecha el peek



    /**
     * Uso de pattern matching para capturar un Either.Left
     */
    @Test
    public void testPatternMatchingLeftSide(){
        Option<String> none = None();

        //toEither method transform Option<String> to String
        Either<String, String> left = none.toEither(() -> "Left from None value");

        String result = Match(left).of(
                Case($Left($()), msg -> msg),
                Case($(), "Not found")
        );
        assertEquals("Failure - Pattern of either left doesn't catch the left projection", left.getLeft(), result);
    }

    /**
     * Uso de pattern matching para capturar un Either.Right
     */
    @Test
    public void testPatternMatchingRightSide(){
        Option<String> value = Option.of("Right value rules!");
        //toEither method transform Option<String> to String
        Either<String, String> right = value.toEither(() -> "Left from None value");
        String result = Match(right).of(
                Case($Right($()), msg -> msg),
                Case($(), "Not found")
        );
        assertEquals("Failure - Pattern of either right doesn't catch the left projection", right.getOrElse(""), result);
    }

    /**
     * Uso de narrow para duplicar objetos Either.
     */
    @Test
    public void testNarrow(){
        Either<Integer, String> either = Try.of(()-> "0").toEither(0);
        Either<Object, Object> copy = Either.narrow(either);
        assertEquals("Failure - the result of narrows must be equals to the source",either,copy);
        assertSame("Failure - Although the narrowed and the source have a different type specification, they must be the same object",
                either,
                copy);
    }

    /**
     * Las proyecciones de Either se pueden transformar con fold.
     * Retorna por defecto la proyección derecha si el Either tiene dicha proyección.
     */
    @Test
    public void testFoldRight(){
        String[] actual = transform("text_to_transform", true).fold(l -> l.split("_"), r -> r.split("_"));
        String[] expected = {"TEXT", "TO", "TRANSFORM"};
        assertArrayEquals("The arrays were not the same", expected, actual);
    }

    /**
     * Las proyecciones de Either se pueden transformar con fold.
     * Retorna por defecto la proyección derecha si el Either tiene dicha proyección; de lo
     * contrario, retorna la proyección izquierda
     */
    @Test
    public void testFoldLeft(){
        String[] actual = transform("text_to_transform", false).fold(l -> l.split("_"), r -> r.split("_"));
        String[] expected = {"text", "to", "transform"};
        assertArrayEquals("The arrays were not the same", expected, actual);
    }

    /**
     * Método utilitario para las pruebas de fold
     * @param s String a transformar
     * @param hasRightProjection si se le debe asignar valor en la derecha o no
     * @return Either<String, String>
     */
    private Either<String, String> transform(String s, boolean hasRightProjection){
        Either<String, String> either = Either.left(s.toLowerCase());
        if (hasRightProjection) either = Either.right(s.toUpperCase());
        return either;
    }



    /**
     * Aunque la documentación de fold either dice que el tipado de los mapper para cada proyección
     * debe ser igual, aún así, el compilador permite crear los mapper con tipos diferentes.
     */
    @Test
    public void testFoldWithTwoMappers(){
        Serializable rightProjection = transform("text_to_transform", true).fold(l -> l.split("_"), r -> r.length());
        assertEquals("Right projection fold was nos successful","17", rightProjection.toString());
        Serializable leftProjection = transform("text_to_transform", false).fold(l -> l.split("_"), r -> r.length());
        String[] expected = {"text", "to", "transform"};
        assertArrayEquals("Left projection fold was nos successful", expected, (String[])leftProjection);
    }
}