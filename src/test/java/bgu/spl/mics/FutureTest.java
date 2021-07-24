package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<Integer> testFuture;

    @BeforeEach
    public void setUp(){
        testFuture= new Future<Integer>();
    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        //fail("Not a good test");
        testIsDone();
        testGetNoParam();
        testResolve();

        testGetTimeUnitParam();
        //testFuture.isDone();
        //testFuture.get();
        //testFuture.get();
        //testFuture.resolve();
    }

    public void testGetNoParam()
    {
        testFuture.resolve(10);
        assertEquals(testFuture.get(),(Integer)10);
    }

    public void testResolve()
    {

        testFuture.resolve(5);
        assertTrue(testFuture.get()==5);
    }

    public void testIsDone(){

        assertFalse(testFuture.isDone());
        testFuture.resolve(9);
        assertTrue(testFuture.isDone());
    }

    public void testGetTimeUnitParam()
    {
        long a=System.currentTimeMillis();
        assertNull(testFuture.get(2, TimeUnit.SECONDS));
        testFuture.resolve(3);
        assertEquals(testFuture.get(2,TimeUnit.SECONDS),(Integer)3);
    }


}
