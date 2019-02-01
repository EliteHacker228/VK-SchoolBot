package com.max.example.files.datanodes.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;

@Entity
@Table(name = "classes")
public class SClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name="number")
    private Integer number;

    @Column(name="letter")
    private String letter;

    @Column(name="school_id")
    private Integer schoolId;

    public SClass(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson json =  gsonBuilder.create();
        return json.toJson(this);
    }
}
