package com.example.mongodbstudents.executors;

import com.example.mongodbstudents.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
//Error if @Component is not mentioned
//Field studentsExecutor in com.example.mongodbstudents.controllers.StudentController required a bean of type
//'com.example.mongodbstudents.executors.StudentsExecutor' that could not be found.
public class StudentsExecutor {

    @Autowired
    StudentRepository studentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsExecutor.class);
    public HttpEntity<?> getRecords(){
        LOGGER.info("Finding students record: " + LocalDateTime.now());
        return new HttpEntity<>(studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
//        return new ResponseEntity(HttpStatus.FOUND, );
    }
}
