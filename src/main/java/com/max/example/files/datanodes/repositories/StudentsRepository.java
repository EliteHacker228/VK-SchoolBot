package com.max.example.files.datanodes.repositories;

import com.max.example.files.datanodes.classes.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentsRepository extends CrudRepository<Student, Integer>{

    List<Student> findByVkId(Integer vkId);
}
