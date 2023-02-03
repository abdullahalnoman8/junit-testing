package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
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

        assertTrue(deletedCollegeStudent.isPresent(), "Return true");

        studentService.deleteStudent(1);

        deletedCollegeStudent = studentDao.findById(1);

        assertFalse(deletedCollegeStudent.isPresent(),"Return False");
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

        assertTrue(mathGrades.iterator().hasNext(),"Student has math grades");
        assertTrue(scienceGrades.iterator().hasNext(),"Student has science grades");
        assertTrue(historyGrades.iterator().hasNext(),"Student has history grades");
    }

    @Test
    public void createGradeServiceReturnFalse(){
        assertFalse(studentService.createGrade(105,1,"math"));
        assertFalse(studentService.createGrade(-5,1,"math"));
        assertFalse(studentService.createGrade(-5,2,"math"));
        assertFalse(studentService.createGrade(5,2,"literature"));
    }
    @AfterEach
    public void setupAfterTransaction(){
        jdbcTemplate.execute("delete from student");
        jdbcTemplate.execute("delete from math_grade");
        jdbcTemplate.execute("delete from science_grade");
        jdbcTemplate.execute("delete from history_grade");
    }
}
