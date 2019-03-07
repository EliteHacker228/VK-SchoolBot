package com.max.example.files.datanodes.repositories;

import com.max.example.files.datanodes.classes.SClass;
import com.max.example.files.datanodes.classes.School;
import com.max.example.files.datanodes.classes.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SchoolsRepository extends CrudRepository<School, Integer>{
    List<School> findByName(String name);
    List<School> findByLowerCaseName(String lowerCaseName);

}
