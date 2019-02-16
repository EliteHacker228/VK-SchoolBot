package com.max.example.files.datanodes.classes;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;

@Entity
@Table(name="privatekeys")
public class PrivateKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    Integer id;

    @Column(name="key")
    String key;

    @Column(name="role")
    String role;

    public PrivateKey(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        String privateKey = DigestUtils.sha256Hex(key);
        this.key = privateKey;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
