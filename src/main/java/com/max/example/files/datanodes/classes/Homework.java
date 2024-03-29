package com.max.example.files.datanodes.classes;

import javax.persistence.*;

@Entity
@Table(name="homeworks")
public class Homework {
    @Id
    @SequenceGenerator(name="homework_sequence",sequenceName="homework_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="homework_sequence")
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

    @Column(name="is_reminded")
    private Boolean isReminded;

    public Homework(){
        this.isReminded=false;
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

    public Boolean getReminded() {
        return isReminded;
    }

    public void setReminded(Boolean reminded) {
        isReminded = reminded;
    }
}
