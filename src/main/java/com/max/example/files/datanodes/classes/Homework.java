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

    @Column(name="date")
    private String date;

    @Column(name="remind_date")
    private String remindDate;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(String remindDate) {
        this.remindDate = remindDate;
    }
}
