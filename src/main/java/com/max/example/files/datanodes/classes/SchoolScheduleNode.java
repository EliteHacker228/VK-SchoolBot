package com.max.example.files.datanodes.classes;

import javax.persistence.*;

@Entity
@Table(name = "scheduleNodes")
public class SchoolScheduleNode{

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private String id;

    @Column(name = "classId")
    private Integer classId;

    @Column(name = "className")
    private String className;

    @Column(name = "lessons")
    private String lessons;

    @Column(name = "changes")
    private String changes;

    @Column(name = "day")
    private String day;

    public SchoolScheduleNode(){}

    public SchoolScheduleNode(String className, String lessons, String day) {
        this.id=classId+"" +className+" "+day;
        this.classId=classId;
        this.className = className;
        this.lessons = lessons;
        this.day = day;
    }

    public SchoolScheduleNode(Integer classId, String className, String lessons, String day) {
        //this.id=classId+"" +className+" "+day;
        this.classId=classId;
        this.className = className;
        this.lessons = lessons;
        this.day = day;
    }

    public String toString(){
        String result = "";
        result+="День: "+day.substring(0,1).toUpperCase()+day.substring(1)+"\n";
        int i = 1;
        for(String lesson: lessons.split(",")){
            result+=i+". "+lesson.substring(1,2).toUpperCase()+lesson.substring(2)+"\n";
            i++;
        }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getFormattedChanges(){
        String result = "";
        result+="День: "+day.substring(0,1).toUpperCase()+day.substring(1)+"\n";
        int i = 1;
        for(String change: changes.split(",")){
            result+=i+". "+change.substring(1,2).toUpperCase()+change.substring(2)+"\n";
            i++;
        }
        return result;
    }
}