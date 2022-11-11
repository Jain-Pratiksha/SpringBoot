package com.example.mongodbstudents.services;

import com.example.mongodbstudents.dao.studentsDAO;
import com.example.mongodbstudents.exception.studentNotFound;
import com.example.mongodbstudents.models.StudentsDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

//Error if @Component is not mentioned:
// Consider defining a bean of type 'com.example.mongodbstudents.services.studentsService' in your configuration.
@Component
public class studentsService {


    @Autowired
    studentsDAO studentsDAOImpl;

    public StudentsDB getStudentName(String studentname){
        Optional<StudentsDB> studentName = Optional.ofNullable(studentsDAOImpl.findByStudentName(studentname));
        if (studentName.isPresent()) {
            return studentName.get();
        }
        else {
            throw new studentNotFound("Student not found with name: "+studentname);
            //("Record not found with this student: " + studentname)
            //throw new studentNotFound("Student not found with name: "+studentname);
            //throw new RuntimeException("Student not found with name: "+studentname);
        }
        //return studentsDAOImpl.findByStudentName(studentname);
    }
}
