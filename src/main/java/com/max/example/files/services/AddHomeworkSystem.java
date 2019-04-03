package com.max.example.files.services;

import com.max.example.files.datanodes.classes.Homework;
import com.max.example.files.datanodes.classes.SClass;
import com.max.example.files.datanodes.classes.Student;
import com.max.example.files.datanodes.repositories.ClassesRepository;
import com.max.example.files.datanodes.repositories.HomeworkRepository;
import com.max.example.files.datanodes.repositories.StudentsRepository;
import com.max.example.files.entities.VKRequest;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddHomeworkSystem {
    private StudentsRepository studentsRepository;
    private ClassesRepository classesRepository;
    private HomeworkRepository homeworkRepository;
    private String message;
    private TransportClient transportClient;
    private VkApiClient vk;
    private GroupActor actor;
    private VKRequest vkRequest;


    public AddHomeworkSystem(String message, VKRequest vkRequest, StudentsRepository studentsRepository, ClassesRepository classesRepository, HomeworkRepository homeworkRepository) {
        this.message = message;
        this.studentsRepository = studentsRepository;
        this.classesRepository = classesRepository;
        this.homeworkRepository = homeworkRepository;
        this.vkRequest = vkRequest;
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new GroupActor(177305058, "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e");

    }

    public void workMethod() {
        String text = message;


        try {
            if (text.equals("0")) {
                sendMessage("Отправка отменена", vkRequest.getObject().getFrom_id());
                return;
            }

            String inBrackets = text.substring(text.lastIndexOf("(")).split("[()]")[1].replace(" ", "");//содержимое скобок
            String outOfBrackets = text.substring(0, text.lastIndexOf("(")); //текст за скобками
            //System.out.println("MS:"+inBrackets);

            System.out.println("IN BRACKETS: " + inBrackets);
            System.out.println("OUT OF BRACKETS: " + outOfBrackets);

            SimpleDateFormat sf = new SimpleDateFormat("dd.MM");


            if (inBrackets.contains("-")) {
                //System.out.println(text);
                System.out.println("Задание: " + outOfBrackets);
                System.out.println("Отправим задание параллелям: ");
                for (String s : inBrackets.split(";")[0].split("-")) {
                    System.out.print(s.trim() + " ");
                }
                System.out.println();
                System.out.println("Задание должно быть выполнено к " + inBrackets.split(";")[1]);

                int val1 = Integer.parseInt(inBrackets.split(";")[0].split("-")[0]);
                int val2 = Integer.parseInt(inBrackets.split(";")[0].split("-")[1]);

                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();
                for (SClass sClass : classesRepository.findBySchoolId(schoolId)) {
                    for (int i = Math.min(val1, val2); i <= Math.max(val1, val2); i++) {
                        if (sClass.getNumber() == i) {
                            for (Student s : studentsRepository.findByClassId(sClass.getId())) {
                                if(s.getRole().equals("ADMIN")){
                                    continue;
                                }

                                Homework homework = new Homework();
                                homework.setOwnerId(s.getVkId());
                                homework.setTaskText(outOfBrackets);

                                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                                String date = inBrackets.split(";")[1] + "." + new GregorianCalendar().get(Calendar.YEAR);
                                Date dDate = df.parse(date); //дата сдачи

                                homework.setDate(dDate.getTime());//
                                homework.setRemindDate(dDate.getTime() - 86400000L);//date-сутки
                                //if(sf.format(dDate.getTime() - 86400000L).equals(sf.format(new Date())))
                                //if(homework.getRemindDate()-homework.getDate())//дата напоминания минус дата сдачи
                                GregorianCalendar g = new GregorianCalendar();
                                g.add(GregorianCalendar.DAY_OF_MONTH, +1);

                                if(sf.format(new Date()).equals(sf.format(dDate)) || sf.format(g.getTimeInMillis()).equals(homework.getRemindDate())){
                                    homework.setReminded(true);
                                }
                                homeworkRepository.save(homework);
                                sendMessage("\uD83D\uDCACВам поступило новое домашнее задание: \n" + outOfBrackets + " (" + sf.format(homework.getDate()) + ") ", s.getVkId());
                            }
                        }
                    }
                }


            } else if (inBrackets.contains("!")) { //работает
                //System.out.println(text);
                System.out.println("Задание: " + outOfBrackets);
                System.out.println("Отправим задание параллели: ");
                System.out.println(inBrackets.split("!")[0]);
                System.out.println();
                System.out.println("Задание должно быть выполнено к " + inBrackets.split(";")[1]);

                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();
                int classNumber = Integer.parseInt(inBrackets.split("!")[0]);

                for (SClass sClass : classesRepository.findBySchoolId(schoolId)) {
                    if (sClass.getNumber() == classNumber) {
                        for (Student s : studentsRepository.findByClassId(sClass.getId())) {
                            if(s.getRole().equals("ADMIN")){
                                continue;
                            }

                            Homework homework = new Homework();
                            homework.setOwnerId(s.getVkId());
                            homework.setTaskText(outOfBrackets);

                            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                            String date = inBrackets.split(";")[1] + "." + new GregorianCalendar().get(Calendar.YEAR);
                            Date dDate = df.parse(date);

                            homework.setDate(dDate.getTime());//
                            homework.setRemindDate(dDate.getTime() - 86400000L);//date-сутки

                            GregorianCalendar g = new GregorianCalendar();
                            g.add(GregorianCalendar.DAY_OF_MONTH, +1);

                            if(sf.format(new Date()).equals(sf.format(dDate)) || sf.format(g.getTimeInMillis()).equals(homework.getRemindDate())){
                                homework.setReminded(true);
                            }
                            homeworkRepository.save(homework);
                            sendMessage("\uD83D\uDCACВам поступило новое домашнее задание: \n" + outOfBrackets + " (" + sf.format(homework.getDate()) + ") ", s.getVkId());
                        }
                    }

                }

            } else if (inBrackets.split(";")[0].contains(",")) { //работает
                //System.out.println(text);
                System.out.println("Задание: " + outOfBrackets);
                System.out.println("Отправим задание классам: ");
                for (String s : inBrackets.split(";")[0].split(",")) {
                    System.out.print(s.trim() + " ");
                }
                System.out.println();
                System.out.println("Задание должно быть выполнено к " + inBrackets.split(";")[1]);

                String[] classes = inBrackets.split(";")[0].split(","); //список классов через запятую
                //int val1 = Integer.parseInt(inBrackets.split(";")[0].split("-")[0]);
                //int val2 = Integer.parseInt(inBrackets.split(";")[0].split("-")[1]);

                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();
                for (SClass sClass : classesRepository.findBySchoolId(schoolId)) {

                    for (String classLiteral : classes) {
                        classLiteral = classLiteral.replace("-", "").trim();

                        if (Integer.parseInt(classLiteral.substring(0, classLiteral.length() - 1)) == sClass.getNumber() &&
                                classLiteral.substring(classLiteral.length() - 1).toLowerCase().equals(sClass.getLetter().toLowerCase())) {

                            for (Student s : studentsRepository.findByClassId(sClass.getId())) {
                                if(s.getRole().equals("ADMIN")){
                                    continue;
                                }

                                Homework homework = new Homework();
                                homework.setOwnerId(s.getVkId());
                                homework.setTaskText(outOfBrackets);

                                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                                String date = inBrackets.split(";")[1] + "." + new GregorianCalendar().get(Calendar.YEAR);
                                Date dDate = df.parse(date);

                                homework.setDate(dDate.getTime());//
                                homework.setRemindDate(dDate.getTime() - 86400000L);//date-сутки

                                GregorianCalendar g = new GregorianCalendar();
                                g.add(GregorianCalendar.DAY_OF_MONTH, +1);

                                if(sf.format(new Date()).equals(sf.format(dDate)) || sf.format(g.getTimeInMillis()).equals(homework.getRemindDate())){
                                    homework.setReminded(true);
                                }
                                homeworkRepository.save(homework);
                                sendMessage("\uD83D\uDCACВам поступило новое домашнее задание: \n"  + outOfBrackets + " (" + sf.format(homework.getDate()) + ") ", s.getVkId());
                            }
                        }
                    }
                }


            } else if (messageOneClassValidator(text)) { //для одного класса
                //System.out.println(text);
                System.out.println("Задание: " + outOfBrackets);
                System.out.println("Отправим задание классам: ");
                System.out.println(inBrackets.split(";")[0]);
                System.out.println();
                System.out.println("Задание должно быть выполнено к " + inBrackets.split(";")[1]);

                String className = inBrackets.split(";")[0];
                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();

                int classNumber = Integer.parseInt(className.substring(0, inBrackets.split(";")[0].length() - 1));
                String classLetter = className.substring(className.length() - 1);

                for (SClass sClass : classesRepository.findBySchoolId(schoolId)) {
                    if (sClass.getNumber() == classNumber && sClass.getLetter().toLowerCase().equals(classLetter.toLowerCase())) {
                        for (Student s : studentsRepository.findByClassId(sClass.getId())) {
                            if(s.getRole().equals("ADMIN")){
                                continue;
                            }

                            Homework homework = new Homework();
                            homework.setOwnerId(s.getVkId());
                            homework.setTaskText(outOfBrackets);

                            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                            String date = inBrackets.split(";")[1] + "." + new GregorianCalendar().get(Calendar.YEAR);
                            Date dDate = df.parse(date);

                            homework.setDate(dDate.getTime());//
                            homework.setRemindDate(dDate.getTime() - 86400000L);//date-сутки

                            GregorianCalendar g = new GregorianCalendar();
                            g.add(GregorianCalendar.DAY_OF_MONTH, +1);

                            if(sf.format(new Date()).equals(sf.format(dDate)) || sf.format(g.getTimeInMillis()).equals(sf.format(homework.getRemindDate()))){
                                homework.setReminded(true);
                            }
                            homeworkRepository.save(homework);
                            sendMessage("\uD83D\uDCACВам поступило новое домашнее задание: \n"  + outOfBrackets + " (" + sf.format(homework.getDate()) + ") ", s.getVkId());
                        }
                        break;
                    }
                }

            } else {
                sendMessage("Неверный формат команды", vkRequest.getObject().getFrom_id());
                return;
                //sendMessage("Отправлено", vkRequest.getObject().getFrom_id());
            }
        } catch (Exception e) {
            sendMessage("Неверный формат команды", vkRequest.getObject().getFrom_id());
            return;
        }

        sendMessage("Отправлено", vkRequest.getObject().getFrom_id());
    }

    private boolean messageOneClassValidator(String text) {
        try {
            String splittedText = text.substring(text.lastIndexOf("(")).split("[()]")[1]; //содержимое скобок
            String substring = splittedText.split(";")[0].substring(0, splittedText.split(";")[0].length());
            try {
                Integer.parseInt(substring);
            } catch (NumberFormatException e) {
                return true;
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private void sendMessage(String text, int userId) {
        try {
            vk.messages().send(actor).userId(userId).message(text).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
