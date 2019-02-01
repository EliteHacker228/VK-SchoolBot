package com.max.example.files.datanodes.repositories;

import com.max.example.files.datanodes.classes.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface RegionsRepository extends CrudRepository<Region, Long>{

}
