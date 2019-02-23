package com.max.example.files.datanodes.classes;

import javax.persistence.*;

@Entity
@Table(name = "scheduleNodes")
public class SchoolScheduleNode{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "classId")
    private Integer classId;

    @Column(name = "className")
    private String className;

    @Column(name = "lessons")
    private String lessons;

    @Column(name = "day")
    private String day;

    public SchoolScheduleNode(){}

    public SchoolScheduleNode(String className, String lessons, String day) {
        this.classId=classId;
        this.className = className;
        this.lessons = lessons;
        this.day = day;
    }

    public SchoolScheduleNode(Integer classId, String className, String lessons, String day) {
        this.classId=classId;
        this.className = className;
        this.lessons = lessons;
        this.day = day;
    }

    public String toString(){
        String result = "Имя класса: "+className+"\n";
        result+="День: "+day+"\n"+"Уроки: \n"+lessons;
//        for(String lesson: lessons.split(",")){
//            result+="\t"+lesson.trim()+"\n";
//        }
        return result;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLessons() {
        return lessons;
    }

    public void setLessons(String lessons) {
        this.lessons = lessons;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassLetter(){
        return className.substring(className.length()-1);
    }

    public Integer getClassNumber(){
        return Integer.parseInt(className.substring(0,className.length()-1));
    }
}