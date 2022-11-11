package com.example.mongodbstudents.daoimpl;

import com.example.mongodbstudents.models.StudentsDB;
import com.example.mongodbstudents.repository.StudentRepository;
import com.example.mongodbstudents.dao.studentsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class studentsImpl implements studentsDAO{

    @Autowired
    StudentRepository studentRepository;

    @Override
    public StudentsDB findByStudentName(String sName){
        return studentRepository.findByStudentName(sName);
    }
}
