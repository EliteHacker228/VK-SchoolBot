package com.max.example.files.datanodes.repositories;

import com.max.example.files.datanodes.classes.SchoolScheduleNode;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SchoolScheduleRepository extends CrudRepository<SchoolScheduleNode, Integer>{
    List<SchoolScheduleNode> findByClassId(Integer classId);
    List<SchoolScheduleNode> findByClassName(String className);
    List<SchoolScheduleNode> findById(String Id);
}
