package com.max.example.files.datanodes.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.max.example.files.entities.StudentsRoles;

import javax.persistence.*;

@Entity
@Table(name="students")
public class Student{
    @Id
    @SequenceGenerator(name="students_sequence",sequenceName="students_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="students_sequence")
    @Column(name="id")
    private Integer id;

    @Column(name="vk_id")
    private Integer vkId;

    @Column(name="role")
    private String role;

    @Column(name="status")
    private String status;

    @Column(name="class_id")
    private Integer classId;

    @Column(name="school_id")
    private Integer schoolId;

    @Column(name="region_id")
    private Integer regionId;

    public Student(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVkId() {
        return vkId;
    }

    public void setVkId(Integer vkId) {
        this.vkId = vkId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId =schoolId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson json =  gsonBuilder.create();
        return json.toJson(this);
    }
}
