package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {
    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradesDao;
    @BeforeEach
    public void setupDatabase(){
        jdbcTemplate.execute("insert into student(id, firstname,lastname, email_address)" + "" +
                "values (1,'Eric','Roby', 'roby@gmail.com')");
        jdbcTemplate.execute("insert into math_grade(id,student_id, grade) values (1,1,100.00)");
        jdbcTemplate.execute("insert into science_grade(id,student_id, grade) values (1,1,100.00)");
        jdbcTemplate.execute("insert into history_grade(id,student_id, grade) values (1,1,100.00)");
    }
    @Test
    public void createStudentService(){
        studentService.createStudent("Chad","Darby", "darby@gmail.com");

        CollegeStudent student = studentDao.findByEmailAddress("darby@gmail.com");

        assertEquals("darby@gmail.com",student.getEmailAddress(),"find by email");
    }

    @Test
    public void isStudentNullCheck(){
        assertTrue(studentService.checkIfStudentIsNull(1));

        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentService(){
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
        Optional<MathGrade> deletedMathGrade = mathGradesDao.findById(1);
        Optional<ScienceGrade> deletedScienceGrade = scienceGradesDao.findById(1);
        Optional<HistoryGrade> deletedHistoryGrade = historyGradesDao.findById(1);

        assertTrue(deletedCollegeStudent.isPresent(), "Return true");
        assertTrue(deletedMathGrade.isPresent(), "Return true");
        assertTrue(deletedScienceGrade.isPresent(), "Return true");
        assertTrue(deletedHistoryGrade.isPresent(),"Return true");

        studentService.deleteStudent(1);



        deletedCollegeStudent = studentDao.findById(1);
        deletedMathGrade = mathGradesDao.findById(1);
        deletedScienceGrade = scienceGradesDao.findById(1);
        deletedHistoryGrade = historyGradesDao.findById(1);

        assertFalse(deletedCollegeStudent.isPresent(),"Return False");
        assertFalse(deletedMathGrade.isPresent(),"Return False");
        assertFalse(deletedScienceGrade.isPresent(),"Return False");
        assertFalse(deletedHistoryGrade.isPresent(),"Return False");
    }

    @Sql("/insertData.sql")
    @Test
    public void getGradeBookService(){
        Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradeBook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();
        for (CollegeStudent collegeStudent : iterableCollegeStudents) {
            collegeStudents.add(collegeStudent);
        }

        assertEquals(6, collegeStudents.size());
    }

    @DisplayName("Create Grades")
    @Test
    public void createGradeService(){
        // Create the grade
        assertTrue(studentService.createGrade(80.5,1,"math"));
        assertTrue(studentService.createGrade(80.5,1,"science"));
        assertTrue(studentService.createGrade(80.5,1,"history"));
        // get all the grade with studentId
        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradeByStudentId(1);

        // verify there is grades

        assertTrue( ((Collection<MathGrade>) mathGrades).size() == 2,"Student has math grades");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2,"Student has science grades");
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2,"Student has history grades");
    }

    @Test
    public void createGradeServiceReturnFalse(){
        assertFalse(studentService.createGrade(105,1,"math"));
        assertFalse(studentService.createGrade(-5,1,"math"));
        assertFalse(studentService.createGrade(-5,2,"math"));
        assertFalse(studentService.createGrade(5,2,"literature"));
    }

    @Test
    public void deleteGradeService(){
        assertEquals(1, studentService.deleteGrade(1,"math"), "Return student id after delete");
        assertEquals(1, studentService.deleteGrade(1,"science"), "Return student id after delete");
        assertEquals(1, studentService.deleteGrade(1,"history"), "Return student id after delete");
    }

    @Test
    public void deleteGradeServiceReturnStudentIdOfZero(){
        assertEquals(0, studentService.deleteGrade(0,"math"), "No student should have 0 id");
        assertEquals(0, studentService.deleteGrade(0,"literature"), "No student should have a " +
                "literature class");
    }

    @Test
    public void studentInformation(){
        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);

        assertNotNull(gradebookCollegeStudent,"Should not null");
        assertEquals(1,gradebookCollegeStudent.getId());
        assertEquals("Eric",gradebookCollegeStudent.getFirstname());
        assertEquals("Roby",gradebookCollegeStudent.getLastname());
        assertEquals("roby@gmail.com",gradebookCollegeStudent.getEmailAddress());

        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
        assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
    }

    @Test
    public void studentInformationReturnNull(){
        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);
        assertNull(gradebookCollegeStudent,"Return null");
    }
    @AfterEach
    public void setupAfterTransaction(){
        jdbcTemplate.execute("delete from student");
        jdbcTemplate.execute("delete from math_grade");
        jdbcTemplate.execute("delete from science_grade");
        jdbcTemplate.execute("delete from history_grade");
    }
}
