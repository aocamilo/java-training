package co.com.s4n.training.java;

import io.vavr.control.Either;

import java.util.function.Consumer;

public class biPeek {

    public static Either<String, String> biPeek2(Either<String,String> a, Consumer<String> x, Consumer<String> y){
        return a.isRight()? a.peek(x): a.peekLeft(y);
    }


}
