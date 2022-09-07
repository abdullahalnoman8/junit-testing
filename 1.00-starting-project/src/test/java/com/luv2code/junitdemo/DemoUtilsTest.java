package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEach(){
        demoUtils = new DemoUtils();
        System.out.println("BeforeEach executes before the execution of each test method");
    }
    @AfterEach
    void tearDownAfterEach(){
        System.out.println("Running @AfterEach");
        System.out.println();
    }
    @BeforeAll
    static void setupBeforeEachClass(){
        System.out.println("@BeforeAll executes only once before all test methods execution in the class");
    }

    @AfterAll
    static void tearDownAfterAll(){
        System.out.println("@AfterAll executes only once after all test method execution in the class");
    }
    @Test
    void testEqualsAndNotEquals(){

        System.out.println("Running test: testEqualsAndNotEquals");

        //DemoUtils demoUtils = new DemoUtils();
        assertEquals(6, demoUtils.add(2,4),"2 + 4 must be 6");
        assertNotEquals(8,demoUtils.add(6,1)," 6 + 1 must not be 8");
    }

    @Test
    void testNullAndNotNull(){

        System.out.println("Running test: testEqualsAndNotEquals");
       // DemoUtils demoUtils = new DemoUtils();
        String str1 = null;
        String str2 = "Luv2Code";

        assertNull(demoUtils.checkNull(str1), "Object should be null");
        assertNotNull(demoUtils.checkNull(str2),"Object should not be null");
    }
}
