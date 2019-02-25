package com.max.example.files.datanodes.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;

@Entity
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "region_id")
    private Integer regionId;

    @Column(name="visible")
    private boolean visible;

    public School(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson json =  gsonBuilder.create();
        return json.toJson(this);
    }
}
