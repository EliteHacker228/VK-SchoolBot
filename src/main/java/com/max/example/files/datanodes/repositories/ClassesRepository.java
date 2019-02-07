package com.max.example.files.datanodes.repositories;

import com.max.example.files.datanodes.classes.SClass;
import com.max.example.files.datanodes.classes.School;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassesRepository  extends CrudRepository<SClass, Integer>{

    List<SClass> findByNumber (Integer number);
    List<SClass> findByLetter (String letter);
    List<SClass> findBySchoolId (Integer schoolId);
    List<SClass> findByIds (Integer id);

}
