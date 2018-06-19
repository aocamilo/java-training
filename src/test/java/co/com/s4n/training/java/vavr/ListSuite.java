package co.com.s4n.training.java.vavr;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.NoSuchElementException;

import static io.vavr.collection.Iterator.empty;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;


/**
 *  Getting started de la documentacion de vavr http://www.vavr.io/vavr-docs/#_collections
 *  Javadoc de vavr collections https://static.javadoc.io/io.vavr/vavr/0.9.0/io/vavr/collection/package-frame.html
 */

/* Conjuntos datos y listas de datos
*  Mapa -> Clave -> valor
*  Lista -> conjuntos de datos enlazados uno tras otro
*  Coleccion solo quiero escribir o leer! (operaciones de escritura)
*  Complejidad espacial -> espacio fisico en memoria para hacer operación
*  Complejidad algoritmica temporal -> con respecto al numero de elementos que tenga y el tiempo que se demore
*                                       para realizar la operación.
*
*
* */
@RunWith(JUnitPlatform.class)
public class ListSuite {

    /**
     * Lo que sucede cuando se intenta crear un lista de null
     */
    @Test
    public void testListOfNull() {

        assertThrows(NullPointerException.class, ()->{
            List<String> list1 = List.of(null);
            list1.get();
        });

    }

    /**
     * Lo que sucede cuando se crea una lista vacía y se llama un método
     */

    // Así se crea una lista vacia!!!! >>
    // tupla agrupación de un tipo de datos

    @Test
    public void testZipOnEmptyList() {
        List<String> list = List.of();
        assertTrue(list.isEmpty());
        List<Tuple2<String, Object>> zip = list.zip(empty());

        System.out.println(zip.size());
        assertEquals(zip.size(), 0);
    }

    //Zipper 1a1

    @Test
    public void testingZip(){
        List<Integer> l1 = List.of(1,2,3);
        List<Integer> l2 = List.of(1,2,3);
        List<Tuple2<Integer, Integer>> zip = l1.zip(l2);
        System.out.println("zip -> "+ zip);
        assertEquals(zip.headOption().getOrElse(new Tuple2(0,0)), new Tuple2(1,1));
    }

    // Solo hace zip con la misma cantidad de datos, si una lista tiene mas datos que la otra, estos se descartan.

    @Test
    public void testingZipWithDiffSize(){
        List<Integer> l1 = List.of(1,2,3,4);
        List<Integer> l2 = List.of(1,2,3);
        List<Tuple2<Integer, Integer>> zip = l1.zip(l2);
        System.out.println("zip -> "+ zip);
        assertEquals(zip.headOption().getOrElse(new Tuple2(0,0)), new Tuple2(1,1));
    }

    @Test
    public void testHead(){
        List<Integer> list1 = List.of(1,2,3);
        Integer head = list1.head(); // accesor al primer elemento -> convención estructura de datos func
        assertEquals(head, new Integer(1));
    }

    @Test
    public void testHead2(){
        List<Integer> list1 = List.of();
        Integer head = list1.headOption().getOrElse(1); //equivalente a orElseGet

        assertEquals(new Integer(1), head);
    }

    // No hay elementos, tira una excepción de NoSuchElementException, por esto es peligroso acceder a una lista con Head
    // es mas seguro acceder con un HeadOption

    @Test
    public void EmptyListTestHead(){
        List<Integer> list1 = List.of();
        assertThrows(NoSuchElementException.class, ()->{
            Integer head = list1.head();
            assertEquals(head, null);
        });

    }

    // para comparar option null = Option.none, headOption devuelve un Option
    @Test
    public void EmptyListHeadOptionTest(){
        List<Integer> list1 = List.of();
        Option<Integer> head = list1.headOption();
        assertEquals(head, Option.none());

    }

    @Test
    public void testTail(){
        List<Integer> list1 = List.of(1,2,3);
        List<Integer> expectedTail = List.of(2,3);
        List<Integer> tail = list1.tail(); // Todos menos el primer elemento -> convención estructura datos func
        assertEquals(tail, expectedTail);
    }

    // devuelve lista vacia si llamamos a Tail y tiene solo un elemento

    @Test
    public void OnlyElementTestTail(){
        List<Integer> list1 = List.of(1);
        List<Integer> expectedTail = List.of();
        List<Integer> tail = list1.tail(); // Todos menos el primer elemento -> convención estructura datos func
        assertEquals(tail, expectedTail);
    }

    @Test
    public void testZip(){
        List<Integer> list1 = List.of(1,2,3);
        List<Integer> list2 = List.of(1,2,3);
        List<Tuple2<Integer, Integer>> zippedList = list1.zip(list2);
        assertEquals(zippedList.head(), Tuple.of(new Integer(1), new Integer(1)) );
        assertEquals(zippedList.tail().head(), Tuple.of(new Integer(2), new Integer(2)) );
    }

    /**
     * Una Lista es inmutable
     */
    @Test
    public void testListIsImmutable() {
        List<Integer> list1 = List.of(0, 1, 2);
        List<Integer> list2 = list1.map(i -> i);
        assertEquals(List.of(0, 1, 2),list1);
        assertNotSame(list1,list2);
    }

    public String nameOfNumber(int i){
        switch(i){
            case 1: return "uno";
            case 2: return "dos";
            case 3: return "tres";
            default: return "idk";
        }
    }

    @Test
    public void testMap(){

        List<Integer> list1 = List.of(1, 2, 3);
        List<String> list2 = list1.map(i -> nameOfNumber(i));

        assertEquals(list2, List.of("uno", "dos", "tres"));
        assertEquals(list1, List.of(1,2,3));

    }


    @Test
    public void testFilter(){
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> filteredList = list.filter(i -> i % 2 == 0);
        assertTrue(filteredList.get(0)==2);
    }


    /**
     * Se revisa el comportamiento cuando se pasa un iterador vacío
     */

    // Como es vacia, no tiene pareja, por lo cual el resultado del zip es Empty

    @Test
    public void testZipWhenEmpty() {
        List<String> list = List.of("I", "Mario's", "Please", "me");
        List<Tuple2<String, Integer>> zipped = list.zip(empty());
        assertTrue(zipped.isEmpty());
    }

    /**
     * Se revisa el comportamiento cuando se pasa el iterador de otra lista
     */

    // Mismo caso con una mas grande que otro

    @Test
    public void testZipWhenNotEmpty() {
        List<String> list1 = List.of("I", "Mario's", "Please", "me", ":(");
        List<String> list2 = List.of("deleted", "test", "forgive", "!");
        List<Tuple2<String, String>> zipped2 = list1.zip(list2);
        List<Tuple2<String, String>> expected2 = List.of(Tuple.of("I", "deleted"), Tuple.of("Mario's", "test"),
                Tuple.of("Please", "forgive"), Tuple.of("me", "!"));
        assertEquals(expected2,zipped2);
    }

    /**
     * El zipWithIndex agrega numeración a cada item
     */
    @Test
    public void testZipWithIndex() {
        List<String> list = List.of("A", "B", "C");
        List<Tuple2<String, Integer>> expected = List.of(Tuple.of("A", 0), Tuple.of("B", 1), Tuple.of("C", 2));
        assertEquals(expected,list.zipWithIndex());
    }

    /**
     *  pop y push por defecto trabajan para las pilas.
     */
    @Test
    public void testListStack() {
        List<String> list = List.of("B", "A");

        assertEquals(
                List.of("A"), list.pop());

        assertEquals(
                List.of("D", "C", "B", "A"), list.push("C", "D"));

        assertEquals(
                List.of("C", "B", "A"), list.push("C"));

        assertEquals(
                List.of("B", "A"), list.push("C").pop());

        assertEquals(
                Tuple.of("B", List.of("A")), list.pop2());
    }

    @Test
    public void popWithEmpty(){
        List<Integer> l1 = List.of();

        assertThrows(NoSuchElementException.class, ()->{
            List<Integer> l2 = l1.pop();
            assertEquals(l2, empty());
        });

    }

    @Test
    public void popWithEmpty2(){
        List<Integer> l1 = List.of();
        Option<List<Integer>> l2 = l1.popOption();
        assertEquals(l2, Option.none());
    }

    @Test
    public void popVsTail(){
        List<Integer> l1 = List.of(1,2,3,4,5);
        assertEquals(l1.pop(), l1.tail());
        assertEquals(l1.popOption(), l1.tailOption()); // pop = tail
    }

    @Test
    public void biggerPop2Test(){
        List<Integer> l1 = List.of(1,2,3,4,5,6,7,8,9);
        System.out.println("pop2 -> " + l1.pop2()); // pop2 nos da una tupla del elemento con el head y el tail.
        Tuple2<Integer, List<Integer>> l2 = l1.pop2();
        assertEquals(l2._1.intValue(), 1); // los elementos de una tupla se acceden con _y el primer elemento
        assertEquals(l2._2, List.of(2,3,4,5,6,7,8,9));

    }

    @Test
    public void pop2OnEmptyList(){
        List<Integer> l1 = List.of();
        assertThrows(NoSuchElementException.class, ()-> {
            Tuple2<Integer, List<Integer>> r = l1.pop2();
        });
    }


    /**
     * Una lista de vavr se comporta como una pila ya que guarda y
     * retorna sus elementos como LIFO.
     * Peek retorna el ultimo elemento en ingresar en la lista
     */
    @Test
    public void testLIFORetrieval() {
        List<String> list = List.empty();
        //Because vavr List is inmutable, we must capture the new list that the push method returns
        list = list.push("a");
        list = list.push("b");
        list = list.push("c");
        list = list.push("d");
        list = list.push("e");
        assertEquals( List.of("d", "c", "b", "a"), list.pop());
        assertEquals("e", list.peek()); // peek is head as pop is tail *******
    }

    /**
     * Una lista puede ser filtrada dado un prediacado y el resultado
     * es guardado en una tupla
     */
    @Test
    public void testSpan() {
        List<String> list = List.of("a", "b", "c");
        Tuple2<List<String>, List<String>> tuple = list.span(s -> s.equals("a"));
        assertEquals(List.of("a"), tuple._1);
        assertEquals(List.of("b", "c"), tuple._2);
    }


    /**
     * Validar dos listas con la funcion Takewhile con los predicados el elemento menor a ocho y el elemento mayor a dos
     */
    @Test
    public void testListToTakeWhile() {
        List<Integer> myList = List.ofAll(4, 6, 8, 5);
        List<Integer> myListOne = List.ofAll(2, 4, 3);
        List<Integer> myListRes = myList.takeWhile(j -> j < 8); // guarda en la lista hasta que encuentra un valor mayor a 8 (4,6)
        List<Integer> myListResOne = myListOne.takeWhile(j -> j > 2); // guarda en la lista hasta que encuentra un valor mayor a 2 (vacio)
        assertTrue( myListRes.nonEmpty());
        assertEquals( 2, myListRes.length());
        assertEquals( new Integer(6), myListRes.last());
        assertTrue( myListResOne.isEmpty());
    }

    /**
     * Se puede separar una lista en ventanas de un tamaño especifico
     */
    @Test
    public void testSliding(){
        List<String> list = List.of(
                "First",
                "window",
                "!",
                "???",
                "???",
                "???");
        assertEquals(List.of("First","window","!"),list.sliding(3).head());
    }

    /**
     * Al dividir una lista en ventanas se puede especificar el tamaño del salto antes de crear la siguiente ventana
     */
    @Test
    public void testSlidingWithExplicitStep(){
        List<String> list = List.of(
                "First",
                "window",
                "!",
                "Second",
                "window",
                "!");
        List<List<String>> windows = list.sliding(3,3).toList(); // Iterator -> List
        assertEquals(
                List.of("Second","window","!"),
                windows.get(1));
        List<List<String>> windows2 = list.sliding(3,1).toList(); // Iterator -> List
        assertEquals(
                List.of("window","!","Second"),
                windows2.get(1));
    }

    @Test
    public void testFold(){
        List<Integer> l1 = List.of(1,2,3,4,5);
        Integer r = l1.fold(0, (acc, el) -> acc + el); // recibe el parametro 0 para indicar operacion bianaria
        assertEquals(r.intValue(),15);                  // acc es el acomulador y el es numero actual
    }

    @Test
    public void testFoldLeftAndRight(){
        List<String> l1 = List.of("1","2","3","4","5");
        String r = l1.foldLeft("", (acc, el) -> acc + el);
        String r2 = l1.foldRight("", (el,acc)-> acc+el);

        System.out.println("Left -> " + r);
        System.out.println("Right -> " + r2);
    }

    /*
    Lista vacia como un ciudadano de primer nivel
    map me permite transformar cualquier elemento en lo que quiera
    head y tail como accesores a la lista inseguros
    headOption y tailOption accesores seguros a la lista
    Fold, FoldRight, FoldLeft muy importantes
     */
}