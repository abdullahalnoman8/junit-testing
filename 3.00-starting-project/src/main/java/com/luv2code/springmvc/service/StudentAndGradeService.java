package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {


    @Autowired
    private StudentDao studentDao;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradesDao;

    @Autowired
    private StudentGrades studentGrades;

    public void createStudent(String firstName, String lastName, String emailAddress){
        CollegeStudent student = new CollegeStudent(firstName,lastName,emailAddress);
        student.setId(0);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int id) {
        Optional<CollegeStudent>  student = studentDao.findById(id);
        if(student.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public void deleteStudent(int id) {
       if(checkIfStudentIsNull(id)){
           studentDao.deleteById(id);
           mathGradeDao.deleteByStudentId(id);
           scienceGradesDao.deleteByStudentId(id);
           historyGradesDao.deleteByStudentId(id);
       }
    }

    public Iterable<CollegeStudent> getGradeBook() {
        Iterable<CollegeStudent> collegeStudents = studentDao.findAll();
        return collegeStudents;
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {
        if(!checkIfStudentIsNull(studentId)){
            return false;
        }
        if(grade >= 0 && grade <= 100){
            if(gradeType.equals("math")){
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDao.save(mathGrade);
                return true;
            }
            if(gradeType.equals("science")){
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradesDao.save(scienceGrade);
                return true;
            }
            if(gradeType.equals("history")){
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradesDao.save(historyGrade);
                return true;
            }
        }
        return false;
    }

    public int deleteGrade(int id, String gradeType) {
      int studentId = 0;
      if(gradeType.equals("math")){
          Optional<MathGrade> grade = mathGradeDao.findById(id);
          if(!grade.isPresent()){
              return studentId;
          }
          studentId = grade.get().getStudentId();
          mathGradeDao.deleteById(studentId);
      }
      if(gradeType.equals("science")){
          Optional<ScienceGrade> grade = scienceGradesDao.findById(id);
          if(!grade.isPresent()){
              return studentId;
          }
          studentId = grade.get().getStudentId();
          scienceGradesDao.deleteById(studentId);
      }

      if(gradeType.equals("history")){
          Optional<HistoryGrade> grade = historyGradesDao.findById(id);
          if(!grade.isPresent()){
              return studentId;
          }
          studentId = grade.get().getStudentId();
          historyGradesDao.deleteById(studentId);
      }
      return studentId;
    }

    public GradebookCollegeStudent studentInformation(int id) {
        if(!checkIfStudentIsNull(id)){
            return null;
        }
        Optional<CollegeStudent> student = studentDao.findById(id);
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradeByStudentId(id);

        List<Grade> mathGradesList = new ArrayList<>();
        mathGrades.forEach(mathGradesList::add);

        List<Grade> scienceGradeList = new ArrayList<>();
        scienceGrades.forEach(scienceGradeList::add);

        List<Grade> historyGradesList = new ArrayList<>();
        historyGrades.forEach(historyGradesList::add);

        studentGrades.setMathGradeResults(mathGradesList);
        studentGrades.setScienceGradeResults(scienceGradeList);
        studentGrades.setHistoryGradeResults(historyGradesList);

        GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(student.get().getId(),
                student.get().getFirstname(),student.get().getLastname(),student.get().getEmailAddress(),studentGrades);


        return gradebookCollegeStudent;
    }
}
