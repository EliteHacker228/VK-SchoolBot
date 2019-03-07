package com.max.example.files.services;

import com.max.example.files.datanodes.classes.SClass;
import com.max.example.files.datanodes.classes.Student;
import com.max.example.files.datanodes.repositories.ClassesRepository;
import com.max.example.files.datanodes.repositories.StudentsRepository;
import com.max.example.files.entities.VKRequest;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import java.util.ArrayList;

public class AttentionService {
    private StudentsRepository studentsRepository;
    private ClassesRepository classesRepository;
    private String message;
    private TransportClient transportClient;
    private VkApiClient vk;
    private GroupActor actor;
    private VKRequest vkRequest;


    public AttentionService(String message, VKRequest vkRequest, StudentsRepository studentsRepository, ClassesRepository classesRepository){
        this.message=message;
        this.studentsRepository=studentsRepository;
        this.classesRepository=classesRepository;
        this.vkRequest=vkRequest;
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new GroupActor(177305058, "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e");

    }

    public void workMethod(){
        try {
            String text = message;
            System.out.println("MSG: " + message);
            if (text.equals("0")) {
                sendMessage("Отправка отменена", vkRequest.getObject().getFrom_id());
                return;
            }
            if (text.substring(text.lastIndexOf("(")).split("[()]")[1].contains("*")) {

                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();
                for (SClass sClass : classesRepository.findBySchoolId(schoolId)) {
                    for (Student s : studentsRepository.findByClassId(sClass.getId())) {
                        sendMessage(text.split("[()]")[0].replace("[", "(").replace("]", ")").replace("^", "*"), s.getVkId());
                    }
                }
            } else if (text.substring(text.lastIndexOf("(")).split("[()]")[1].contains("-")) {
                String[] splittedText = text.substring(text.lastIndexOf("(")).split("[()]")[1].split("[()]");
                String neededText = splittedText[0];//классы через -
                String[] sndt = neededText.split("-");
                int val1 = Integer.parseInt(sndt[0]);
                int val2 = Integer.parseInt(sndt[1]);
//            System.out.println("Отправим сообщения всем параллелям с "+
//                    Math.min(val1, val2)+" по "+Math.max(val1, val2)+" класс");
                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();

                for (SClass sClass : classesRepository.findBySchoolId(schoolId)) {
                    for (int i = Math.min(val1, val2); i <= Math.max(val1, val2); i++) {
                        if (sClass.getNumber() == i) {
                            for (Student s : studentsRepository.findByClassId(sClass.getId())) {
                                sendMessage(text.substring(0,text.lastIndexOf("(")).replace("[", "(").replace("]", ")").replace("^", "*"), s.getVkId());
                            }
                        }
                    }
                }

            } else if (text.substring(text.lastIndexOf("(")).split("[()]")[1].contains("!")) { //работает
                //String[] splittedText = text.split("[()]");
                String neededText = text.substring(text.lastIndexOf("(")).split("[()]")[1]; //
                String sndt = neededText.split("!")[0];
                Integer number = Integer.parseInt(sndt);
                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();
                for (SClass sClass : classesRepository.findBySchoolId(schoolId)) {
                    for (Student s : studentsRepository.findByClassId(sClass.getId())) {
                        if (classesRepository.findByid(s.getClassId()).get(0).getNumber() == number) {
                            sendMessage(text.substring(0,text.lastIndexOf("(")).replace("[", "(").replace("]", ")").replace("^", "*"), s.getVkId());
                        }
                    }
                }

            } else if (text.substring(text.lastIndexOf("(")).split("[()]")[1].contains(",")) { //работает
                //String[] splittedText = text.split("[()]");//делим на части (текст)+(классы)
                String neededText = text.substring(text.lastIndexOf("(")).split("[()]")[1];//классы
                String[] sndt = neededText.split(",");//массив классов(работает)

                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();
                //System.out.print("Отправим объявление: ");

                ArrayList<SClass> classes = new ArrayList<>();
                ArrayList<SClass> sClassArrayList = new ArrayList<SClass>(classesRepository.findBySchoolId(schoolId));

                for (String s : sndt) {
                    s=s.trim();
                    String letter = s.trim().substring(s.length() - 1, s.length());//Буква
                    String number = s.trim().substring(0, s.length() - 1);//Число

                    for (SClass sClass1 : sClassArrayList) {
                        if (sClass1.getLetter().equals(letter) &&
                                sClass1.getNumber() == Integer.parseInt(number)) {
                            classes.add(sClass1);
                        }

                    }
                }

                for (SClass sClass : sClassArrayList) {
                    ArrayList<Student> students = new ArrayList<>(studentsRepository.findByClassId(sClass.getId()));
                    for (Student student : students) {
                        sendMessage(text.substring(0, text.lastIndexOf("(")).replace("[", "(").replace("]", ")").replace("^", "*"), student.getVkId());
                    }
                }
                //System.out.println(" классам");

            } else if (messageOneClassValidator(text)) { //Работает
                String[] splittedText = text.split("[()]");
                String s = text.substring(text.lastIndexOf("(")).split("[()]")[1]; //класс

                int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();
                String letter = s.substring(s.length() - 1, s.length());//Буква
                String number = s.substring(0, s.length() - 1);//Число

                ArrayList<SClass> sClassArrayList = new ArrayList<SClass>(classesRepository.findBySchoolId(schoolId));
                for (SClass sClass : sClassArrayList) {
                    if (sClass.getLetter().equals(letter) && sClass.getNumber() == Integer.parseInt(number)) {
                        ArrayList<Student> students = new ArrayList<>(studentsRepository.findByClassId(sClass.getId()));
                        for (Student student : students) {
                            sendMessage(text.substring(0,text.lastIndexOf("(")).replace("[", "(").replace("]", ")").replace("^", "*"), student.getVkId());
                        }
                    }
                }

            } else {
                sendMessage("Неверно указан класс/параллель", vkRequest.getObject().getFrom_id());
                //sendMessage("Отправлено", vkRequest.getObject().getFrom_id());
            }
        }catch (Exception e){
            sendMessage("Неверно указан класс/параллель", vkRequest.getObject().getFrom_id());
        }
    }

    private boolean messageOneClassValidator(String text){
        try {
            String splittedText = text.substring(text.lastIndexOf("(")).split("[()]")[1];
            String substring = splittedText.substring(splittedText.length() - 1);
            try {
                Integer.parseInt(substring);
            } catch (NumberFormatException e) {
                return true;
            }
            return false;
        }catch (ArrayIndexOutOfBoundsException e){
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
