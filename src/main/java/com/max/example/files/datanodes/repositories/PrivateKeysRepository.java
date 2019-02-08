package com.max.example.files.datanodes.repositories;

import com.max.example.files.datanodes.classes.PrivateKey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrivateKeysRepository extends CrudRepository<PrivateKey, Integer> {
    List<PrivateKey> findByKey(String key);
}
