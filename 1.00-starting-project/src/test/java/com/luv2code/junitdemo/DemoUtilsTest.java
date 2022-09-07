package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEach(){
        demoUtils = new DemoUtils();
        System.out.println("@BeforeEach executes before the execution of each test method");
    }

    @AfterEach
    void tearDownAfterEach(){
        System.out.println("Running @AfterEach");
        System.out.println();
    }
    @BeforeAll
    static void setupBeforeEachClass(){
        System.out.println("@BeforeAll executes only once before all test methods execution in the class");
        System.out.println();
    }

    @AfterAll
    static void tearDownAfterAll(){
        System.out.println("@AfterAll executes only once after all test method execution in the class");
    }

    @Test
    @DisplayName("Equals and Not Equals")
    void testEqualsAndNotEquals(){

        System.out.println("Running test: testEqualsAndNotEquals");

        //DemoUtils demoUtils = new DemoUtils();
        assertEquals(6, demoUtils.add(2,4),"2 + 4 must be 6");
        assertNotEquals(8,demoUtils.add(6,1)," 6 + 1 must not be 8");
    }

    @Test
    @DisplayName("Null and Not Null")
    void testNullAndNotNull(){

        System.out.println("Running test: testEqualsAndNotEquals");
       // DemoUtils demoUtils = new DemoUtils();
        String str1 = null;
        String str2 = "Luv2Code";

        assertNull(demoUtils.checkNull(str1), "Object should be null");
        assertNotNull(demoUtils.checkNull(str2),"Object should not be null");
    }


    @Test
    @DisplayName("Same and Not Same")
    void testSameAndNotSame(){
        String str = "luv2code";
        assertSame(demoUtils.getAcademy(),demoUtils.getAcademyDuplicate(), "Object should refer to the same object");

        assertNotSame(str,demoUtils.getAcademy(),"Object should not refer to same object");

    }

    @DisplayName("True and False")
    @Test
    void testTrueFalse(){
       int gradeOne = 10;
       int gradeTwo = 5;

       assertTrue(demoUtils.isGreater(gradeOne,gradeTwo), "This should return true");
       assertFalse(demoUtils.isGreater(gradeTwo,gradeOne),"This should return false");
    }

    @DisplayName("Array Equals")
    @Test
    void testArrayEquals() {
        String[] stringArray = {"A", "B", "C"};

        assertArrayEquals(stringArray, demoUtils.getFirstThreeLettersOfAlphabet(), "Arrays should be the same");
    }

    @DisplayName("Iterable equals")
    @Test
    void testIterableEquals() {
        List<String> theList = List.of("luv", "2", "code");

        assertIterableEquals(theList, demoUtils.getAcademyInList(), "Expected list should be same as actual list");
    }

    @DisplayName("Lines match")
    @Test
    void testLinesMatch() {
        List<String> theList = List.of("luv", "2", "code");

        assertLinesMatch(theList, demoUtils.getAcademyInList(), "Lines should match");
    }

    @DisplayName("Throws and Does Not Throw")
    @Test
    void testThrowsAndDoesNotThrow() {
        assertThrows(Exception.class, () -> { demoUtils.throwException(-1); }, "Should throw exception");

        assertDoesNotThrow(() -> { demoUtils.throwException(5); }, "Should not throw exception");

    }

    @DisplayName("Timeout")
    @Test
    void testTimeout() {

        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> { demoUtils.checkTimeout(); },
                "Method should execute in 3 seconds");
    }

}
