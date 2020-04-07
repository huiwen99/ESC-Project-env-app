package com.example.env;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//Test Listing class if the static methods work fine for the price input
public class UnitTest {
    @Test
    public void priceTextToNumStringPassTest1() {
        String result = Listing.priceTextToNumString("$2");
        assertEquals("2", result);
    }

    @Test
    public void priceTextToNumStringPassTest2() {
        String result = Listing.priceTextToNumString("$0.20");
        assertEquals("0.20", result);
    }

    @Test
    public void priceTextToNumStringPassTest3(){
        String result = Listing.priceTextToNumString("$0.342985");
        assertEquals("0.342985", result );
    }
    @Test
    public void numInputToPriceTextPassTest1() {
        String result = Listing.numInputToPriceText("0.3234983249");
        assertEquals("$0.33", result);
    }

    @Test
    public void numInputToPriceTextPassTest2() {
        String result = Listing.numInputToPriceText("3.532434983249");
        assertEquals("$3.54", result);
    }
    @Test
    public void numInputToPriceTextPassTest3() {
        String result = Listing.numInputToPriceText("3.5");
        assertEquals("$3.50", result);
    }



    //@Test
    //public void priceTextToNumStringFailTest1() {
    //String result = Listing.priceTextToNumString("$3");
    //assertEquals("$3",result);
    //}

    //@Test
    //public void priceTextToNumStringFailTest2() {
    //String result = Listing.priceTextToNumString("$4");
    //assertEquals(null, result);


    //}
}