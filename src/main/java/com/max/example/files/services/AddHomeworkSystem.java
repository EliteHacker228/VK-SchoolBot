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

public class AddHomeworkSystem {
    private StudentsRepository studentsRepository;
    private ClassesRepository classesRepository;
    private String message;
    private TransportClient transportClient;
    private VkApiClient vk;
    private GroupActor actor;
    private VKRequest vkRequest;


    public AddHomeworkSystem(String message, VKRequest vkRequest, StudentsRepository studentsRepository, ClassesRepository classesRepository){
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
            if (text.substring(text.lastIndexOf("(")).split("[()]")[1].contains("!")) { //Отправка ДЗ всей параллели


            } else if (text.substring(text.lastIndexOf("(")).split("[()]")[1].contains(",")) { //Отправка ДЗ классам, указанным через запятую


            } else if (messageOneClassValidator(text)) { //Отправка ДЗ определённому классу

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
