package com.max.example.files.datanodes.repositories;

import com.max.example.files.datanodes.classes.Homework;
import org.springframework.data.repository.CrudRepository;

public interface HomeworkRepository extends CrudRepository<Homework, Integer> {
}
