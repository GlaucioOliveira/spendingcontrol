package com.goliveira.spendingcontrol;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void TestingIncomeGetDescription(){
        Income income = new Income();
        String test = "hi";

        income.setDescription(test);

        Assert.assertEquals(income.getDescription(), test);
    }

}