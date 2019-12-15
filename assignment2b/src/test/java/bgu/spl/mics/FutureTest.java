package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    private Future<T> testFuture;


    @BeforeEach
    public void setUp(){
        testFuture = new Future<T>();
    }




    @Test
    public void testIsDone() {
        assertFalse(testFuture.isDone());
        Agent result = new Agent();
        testFuture.resolve((T) result);
        assertTrue(testFuture.isDone());
        testFuture.resetForTesting();
    }


    @Test
    public void testGet() {
        assertEquals((T) null, testFuture.get());
        Agent result = new Agent();
        testFuture.resolve((T) result);
        assertEquals((T) result, testFuture.get());
        testFuture.resetForTesting();
    }


    @Test
    public void testResolve () {
        Agent result = new Agent();
        testFuture.resolve((T) result);
        assertEquals((T) result, testFuture.get());
        testFuture.resetForTesting();
    }


}


