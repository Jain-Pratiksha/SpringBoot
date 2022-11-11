package com.example.mongodbstudents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MongodbStudentsApplication {

	public static void main(String[] args) {
		System.out.println("\nYou are working with StudentsDB");
		SpringApplication.run(MongodbStudentsApplication.class, args);
	}

}
