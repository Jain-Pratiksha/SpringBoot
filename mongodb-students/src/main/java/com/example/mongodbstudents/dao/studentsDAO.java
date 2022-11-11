package com.example.mongodbstudents.dao;

import com.example.mongodbstudents.models.StudentsDB;
import org.springframework.stereotype.Service;

public interface studentsDAO {

    StudentsDB findByStudentName(String sName);
}
