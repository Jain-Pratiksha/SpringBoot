package com.example.mongodbstudents.repository;

import com.example.mongodbstudents.models.StudentsDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<StudentsDB, Integer> {

//    @Query("{'studentName': ?}")
    StudentsDB findByStudentName(String sName);
}
