package com.example.mongodbstudents.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

//DTO
@Data
@Document(collection = "students")
public class StudentsDB {

    @Id
    @JsonProperty("RollNo") //input from postman as RollNo
    private int id;

    //private String RollNo;

    @Field(write = Field.Write.NON_NULL)
    private String studentName;

    private List<String> courses;

}
