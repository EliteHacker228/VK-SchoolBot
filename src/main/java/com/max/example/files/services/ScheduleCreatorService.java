package com.max.example.files.services;

import com.max.example.files.datanodes.classes.SchoolScheduleNode;

import java.util.ArrayList;

public class ScheduleCreatorService {
    static ArrayList<SchoolScheduleNode> stringToScheduleConverter(String source) throws IllegalArgumentException {
        source = source.toLowerCase();

        if (!source.contains("понедельник") && !source.contains("вторник") &&
                !source.contains("среда") && !source.contains("четверг") &&
                !source.contains("пятница") && !source.contains("суббота") &&
                !source.contains("воскресенье")
                ) {
            throw new IllegalArgumentException("No such day!");
        }

        ArrayList<SchoolScheduleNode> scheduleNodes = new ArrayList<>();

        String[] days = {"понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье"};

        source = source.replace(", ", ",");
        source = source.replace(",", ", ");
        source = source.replace("понедельник", "%#&!понедельник");
        source = source.replace("вторник", "%#&!вторник");
        source = source.replace("среда", "%#&!среда");
        source = source.replace("четверг", "%#&!%четверг");
        source = source.replace("пятница", "%#&!%пятница");
        source = source.replace("суббота", "%#&!суббота");
        source = source.replace("воскресенье", "%#&!воскресенье");

        String[] elements = source.split("%#&!");

        for (int i = 1; i < elements.length; i++) {

            String[] subelement = elements[i].split(":");
            String day = subelement[0].replace("%", "");
            String lessons = subelement[1]; //список уроков через запятую
            SchoolScheduleNode scheduleNode = new SchoolScheduleNode(elements[0].replace(";", ""), lessons, day);

            scheduleNodes.add(scheduleNode);
            //System.out.println(new ScheduleNode(elements[0].replace(";", ""), lessons, day));
        }
        return scheduleNodes;
    }
}
