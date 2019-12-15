package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    private Future<T> testFuture;


    @BeforeEach
    public void setUp(){
        testFuture = new Future<T>();
        MessageBroker testMessageBroker = new MessageBrokerImpl();
    }





    /**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     *
     */

    //
    @Test
    public void testGet() {
        Agent result = new Agent();
        testFuture.resolve(result);
        try{
            testFuture.get();
        } catch (Exception e){
            fail("unexpected expression: "+ e.getMessage());
        }
    }

    /**
     * Resolves the result of this Future object.
     */
    @Test
    public void testResolve (T result) {
        //check if

        try{
            testFuture.resolve(result);
        } catch (Exception e){
            fail("unexpected expression: "+ e.getMessage());
        }
    }

    /**
     * @return true if this object has been resolved, false otherwise
     */
    @Test
    public void testIsDone() {
        testFuture.resolve(T result);
        try {
            testFuture.isDone();
        }
        catch (Exception e){
            fail("unexpected expression: "+ e.getMessage());
        }

    }

    /**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not,
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
    @Test
    public void testGet(long timeout, TimeUnit unit) {
        Agent result = new Agent();
        testFuture.resolve(result);
        try{
            testFuture.get(timeout, unit);
        } catch (Exception e){
            fail("unexpected expression: "+ e.getMessage());
        }
    }


}


