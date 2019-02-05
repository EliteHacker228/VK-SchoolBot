package com.max.example.files.datanodes.repositories;

import com.max.example.files.datanodes.classes.Homework;
import com.max.example.files.datanodes.classes.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HomeworkRepository extends CrudRepository<Homework, Integer> {

    List<Homework> findByOwnerId(Integer ownerId);

}
