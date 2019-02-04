package com.max.example.files.datanodes.classes;

import javax.persistence.*;

@Entity
@Table(name="homeworks")
public class Homework {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name="owner_id")
    private Integer ownerId;

    @Column(name="task_text")
    private String taskText;

    @Column(name="date")
    private Long date;

    @Column(name="remind_date")
    private Long remindDate;

    public Homework(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Long remindDate) {
        this.remindDate = remindDate;
    }
}
