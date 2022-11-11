package com.example.mongodbstudents.exception;

import org.springframework.http.HttpStatus;

public class studentNotFound extends RuntimeException{

    public studentNotFound(String message){
        super(message);
    }

}
