package com.max.example.files.datanodes.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;

@Entity
@Table(name = "regions")
public class Region {

    @Id
    @SequenceGenerator(name="regions_sequence",sequenceName="regions_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="regions_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public Region() {
    }

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

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson json =  gsonBuilder.create();
        return json.toJson(this);
    }
}
