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
        String text=message;
        if(text.contains("*")){
            //System.out.println("Отправим всем классам");
            int schoolId = studentsRepository.findByVkId(vkRequest.getObject().getFrom_id()).get(0).getSchoolId();
            for(SClass sClass: classesRepository.findBySchoolId(schoolId)){
                for(Student s: studentsRepository.findByClassId(sClass.getId())){
                    sendMessage(message, s.getVkId());
                }
            }

//        }else if(text.contains("-")){
//            String[] splittedText = text.split("[()]");
//            String neededText=splittedText[1];
//            String[] sndt = neededText.split("-");
//            int val1 = Integer.parseInt(sndt[0]);
//            int val2 = Integer.parseInt(sndt[1]);
//            System.out.println("Отправим сообщения всем параллелям с "+
//                    Math.min(val1, val2)+" по "+Math.max(val1, val2)+" класс");
//            //System.out.println(splittedText[2]);
//
//        }else if(text.contains("!")){
//            String[] splittedText = text.split("[()]");
//            String neededText=splittedText[1];
//            String sndt=neededText.split("!")[0];
//            System.out.println("Отправим сообщение всем "+sndt+"-м классам");
//
//        }else if(text.contains(",")){
//            String[] splittedText = text.split("[()]");
//            String neededText=splittedText[1];
//            String[] sndt=neededText.split(",");
//            System.out.print("Отправим объявление: ");
//            for(String s: sndt){
//                System.out.print(s);
//            }
//            System.out.println(" классам");
//
//        }else if(messageOneClassValidator(text)){
//            System.out.println("Содержит");
//            messageOneClassValidator(text);
        }else{
            sendMessage("Неверно указан класс/параллель", vkRequest.getObject().getFrom_id());
        }
    }

    private boolean messageOneClassValidator(String text){
        try {
            String splittedText = text.split("[()]")[1];
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
