package com.example.mongodbstudents.controllers;

import com.example.mongodbstudents.executors.StudentsExecutor;
import com.example.mongodbstudents.models.StudentsDB;
import com.example.mongodbstudents.repository.StudentRepository;
import com.example.mongodbstudents.services.studentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@RestController Denotes that the class is the web layer where we can expose the restapi’s (rest endpoints).
@RestController
@RequestMapping("/students/") //after base url add this
public class StudentController {

    @Autowired //if not mentioned it gives null pointer error
    private StudentRepository studentRepository;

    @Autowired
    private studentsService studentsService;

    @Autowired
    private StudentsExecutor studentsExecutor;


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addOne(@RequestBody StudentsDB studentsDB){
        studentRepository.save(studentsDB);
        //resolve this findByID
        return ("Student Data Added\n"+studentsDB);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.FOUND)
    public List<StudentsDB> findAll(){
        System.out.println("Students Data Fetched");
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        /* return studentRepository.findAll().toString();
        Output
        [StudentsDB(RollNo=635fcf868170e344b83865b0, studentName=Pratiksha), StudentsDB(RollNo=635fd0aa295bb05f1d1aff31, studentName=Krutika)
        ]*/
    }

    //HttpEntity Example
    @GetMapping("/records")
    public HttpEntity<?> getRecords(){
        return studentsExecutor.getRecords();
    }


    //Error: Ambiguous handler methods mapped
    //you get that error when @GetMapping("/{id}") @GetMapping("/{sName}") this situation takes place
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Optional<StudentsDB> findOne(@PathVariable int id){

        return studentRepository.findById(id);
    }

    @GetMapping("/studentName/{sName}")
    @ResponseStatus(HttpStatus.FOUND)
    public StudentsDB findBySName(@PathVariable String sName){
        return studentsService.getStudentName(sName);
    }

    @PutMapping("/update/{sName}")
    public String updateStudent(@PathVariable String sName, @RequestBody StudentsDB studentsDB){
        StudentsDB s = studentsService.getStudentName(sName);
        if (s.getStudentName().isEmpty()){
            return "Student not found with name: "+sName;
        }
        studentRepository.deleteById(s.getId());
        studentRepository.save(studentsDB);
        //return String.valueOf(studentsService.getStudentName(sName));
        return "Student data updated: "+studentsDB;
    }

    @PutMapping("/id/{id}")
    public String updateByID(@PathVariable int id, @RequestBody StudentsDB studentsDB){
        Optional<StudentsDB> students = studentRepository.findById(id);
        if (students.isEmpty()){
            return "Not found";
        }
        studentRepository.deleteById(id);
        studentRepository.save(studentsDB);
        return "Updated: "+studentsDB;
    }


    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteOne(@PathVariable int id){
        Optional<StudentsDB> s = studentRepository.findById(id);
        if (s.isEmpty()){
            return "No student with id: "+id;
        }
        else {
            System.out.println(studentRepository.findById(id));
            studentRepository.deleteById(id);
        }
        return ("Student data deleted with id: "+id);
    }
}
