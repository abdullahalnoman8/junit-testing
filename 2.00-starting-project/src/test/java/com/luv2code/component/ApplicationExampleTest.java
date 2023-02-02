package com.luv2code.component;

import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationExampleTest {
    private static int count = 0;
    @Value("${info.app.name}")
    private String appInfo;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.school.name}")
    private String schoolName;

    @Autowired
    private CollegeStudent student;

    @Autowired
    private StudentGrades studentGrades;

    @Autowired
    private ApplicationContext context;
    @BeforeEach
    void beforeEach(){
        count += 1;
        System.out.println("Testing " + appInfo + "which is " + appDescription + "" +
                " Version " + appVersion + ". Execution of test method " + count);

        student.setFirstname("Abdullah");
        student.setLastname("Al Noman");
        student.setEmailAddress("abc@gmail.com");
        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0,85.0,76.0, 91.75)));
        student.setStudentGrades(studentGrades);
    }

    @DisplayName("Add grade results for student grades")
    @Test
    void addGradeResultsForStudentGrades(){
        assertEquals(352.75, studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()
        ));
    }

    @DisplayName("Add grade results for student grades not equal")
    @Test
    void addGradeResultsForStudentGradesNotEquals(){
        assertNotEquals(0,studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("is grade greater")
    @Test
    void isGradeGreaterStudentGrades(){
        assertTrue(studentGrades.isGradeGreater(90,75),"failure - should be true");
    }

    @DisplayName("Is grade greater false")
    @Test
    void isGradeGreaterStudentGradesAssertFalse(){
        assertFalse(studentGrades.isGradeGreater(89,92),"failure - should be false");
    }

    @DisplayName("Check Null for student grades")
    @Test
    void checkNullForStudentGrades(){
        assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()),
                "object should not be null");
    }

    @DisplayName("Create student without grade init")
    @Test
    void createStudentWithoutGradesInit(){
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
        studentTwo.setFirstname("Chad");
        studentTwo.setLastname("Darby");
        studentTwo.setEmailAddress("darby@gmail.com");
        assertNotNull(studentTwo.getFirstname());
        assertNotNull(studentTwo.getLastname());
        assertNotNull(studentTwo.getEmailAddress());
        assertNull(studentGrades.checkNull(studentTwo.getStudentGrades()));
    }

    @DisplayName("Verify students are prototype")
    @Test
    void verifyStudentsArePrototype(){
        CollegeStudent student1 = context.getBean("collegeStudent", CollegeStudent.class);
        assertNotSame(student,student1);
    }

    @DisplayName("Find grade point average")
    @Test
    void findGradePointAverage(){
        assertAll("Testing all assertEquals",
                () -> assertEquals(352.75, studentGrades.addGradeResultsForSingleClass(
                        student.getStudentGrades().getMathGradeResults())),
                () -> assertEquals(88.19, studentGrades.findGradePointAverage(
                        student.getStudentGrades().getMathGradeResults()
                )));
    }

}
