package com.max.example.files.services;

import com.max.example.files.datanodes.classes.Region;
import com.max.example.files.datanodes.classes.SClass;
import com.max.example.files.datanodes.classes.School;
import com.max.example.files.datanodes.classes.Student;
import com.max.example.files.datanodes.repositories.ClassesRepository;
import com.max.example.files.datanodes.repositories.RegionsRepository;
import com.max.example.files.datanodes.repositories.SchoolsRepository;
import com.max.example.files.datanodes.repositories.StudentsRepository;
import com.max.example.files.entities.StudentsRoles;
import com.max.example.files.entities.VKGroupMessage;
import com.max.example.files.entities.VKRequest;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

public class MessageService {
    private TransportClient transportClient;
    private VkApiClient vk;
    private VKRequest vkRequest;
    private VKGroupMessage vkGroupMessage;
    private GroupActor actor;

    private RegionsRepository regionsRepository;
    private ClassesRepository classesRepository;
    private SchoolsRepository schoolsRepository;
    private StudentsRepository studentsRepository;

//    public MessageService(VKRequest vkRequest){
//        this.vkRequest=vkRequest;
//        vkGroupMessage=vkRequest.getObject();
//
//        transportClient = HttpTransportClient.getInstance();
//        vk = new VkApiClient(transportClient);
//
//        actor = new GroupActor(177305058, "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e");
//
//    }

    public MessageService(VKRequest vkRequest, RegionsRepository regionsRepository, ClassesRepository classesRepository, SchoolsRepository schoolsRepository, StudentsRepository studentsRepository) {
        this.vkRequest = vkRequest;
        this.regionsRepository = regionsRepository;
        this.classesRepository = classesRepository;
        this.schoolsRepository = schoolsRepository;
        this.studentsRepository = studentsRepository;

        vkGroupMessage = vkRequest.getObject();

        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        actor = new GroupActor(177305058, "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e");

    }

    private void main(String[] args) {

    }

    public void workMethod() {
        if (studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty()) {
            studentRegistration();
            studentRoleRegistration();

        } else {

            if (studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getRegionId() == null) {
                studentRegionRegistration();
            }

            if (studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getSchoolId() == null) {
                studentSchoolRegistration();
            }

            if (studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getClassId() == null) {
                studentClassRegistration();
            }
        }

        System.out.println("Здравствуйте!");
    }

    private void studentRegistration() {
        Student student = new Student();
        student.setVkId(vkGroupMessage.getFrom_id());
        studentsRepository.save(student);

        System.out.println("Привет! Ты пока не зарегистрирован в нашей системе!" +
                "Для того, чтобы зарегистрироваться, ответь на несколько вопросов.");

        System.out.println("Какой твой регион?");
        int counter = 1;

        String regions = "";
        for (Region region : regionsRepository.findAll()) {
            regions += String.format("%d. " + region.getName(), counter) + "\n";
            counter++;
        }
        System.out.println(regions);
    }

    private void studentRegionRegistration() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);

        if (student.getRegionId() == null) {
            if (vkGroupMessage.getText().matches("[-+]?\\d+")
                    && Integer.parseInt(vkGroupMessage.getText()) <=
                    makeCollection(regionsRepository.findAll()).size()) {
                student.setRegionId(Integer.parseInt(vkGroupMessage.getText()));
                studentsRepository.save(student);
                System.out.println("Регион записан!");
                System.out.println();
            } else {
                System.out.println("Ошибка! Такого региона нет.");
                System.out.println();
                return;
            }

        }

        System.out.println("Из какой ты школы?");
        System.out.println();

        String schools = "";
        int counter = 1;
        for (School school : schoolsRepository.findAll()) {
            schools += String.format("%d. " + school.getName(), counter) + "\n";
            counter++;
        }
        System.out.println(schools);
    }

    private void studentSchoolRegistration() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        if (student.getSchoolId() == null) {
            if (vkGroupMessage.getText().matches("[-+]?\\d+")
                    && Integer.parseInt(vkGroupMessage.getText()) <=
                    makeCollection(regionsRepository.findAll()).size()) {
                student.setSchoolId(Integer.parseInt(vkGroupMessage.getText()));
                studentsRepository.save(student);
                System.out.println("Школа записана!");
            } else {
                System.out.println("Ошибка! Такой школы нет.");
                return;
            }

        }

        System.out.println("Введи свой класс (например, 7Б, 10А и т.д)");

        return;
    }

    private void studentClassRegistration() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        System.out.println("Ввод класса");

        if (student.getClassId() == null) {
            System.out.println("Class id: " + student.getClassId());
            String[] classNode;
            try {
                classNode = stringToClass(vkGroupMessage.getText());
                System.out.println("Message: " + vkGroupMessage.getText());


                for (SClass sClass : classesRepository.findBySchoolId(student.getSchoolId())) {
                    System.out.println(sClass);
                    if (sClass.getNumber() == Integer.parseInt(classNode[0])
                            && sClass.getLetter().equals(classNode[1])
                            && sClass.getSchoolId().equals(student.getSchoolId())
                            ) {

                        student.setClassId(sClass.getId());
                        studentsRepository.save(student);
                        System.out.println("Готово! ");
                    }
                }

            } catch (Exception e) {
                System.out.println("Такого класса нет.");
                return;
            }
        }

        System.out.println("Здравствуйте!");

    }

    private void studentRoleRegistration(){
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        student.setRole(StudentsRoles.STUDENT.name());
        studentsRepository.save(student);
    }


    public <T> Collection<T> makeCollection(Iterable<T> iter) {
        Collection<T> list = new ArrayList<T>();
        for (T item : iter) {
            list.add(item);
        }
        return list;
    }

    public String[] stringToClass(String str) throws Exception {

        str = str.toUpperCase();
        if (str.matches("[-+]?\\d+")) {
            throw new Exception("Wrong class");
        }
        if (str.length() >= 2 && str.length() <= 3) {
            String[] result = {str.substring(0, str.length() - 1),
                    str.substring(str.length() - 1, str.length())
            };
            return result;
        } else {
            throw new Exception("Wrong class");
        }
    }

    public void sendMessage(String text) {
        try {
            vk.messages().send(actor).userId(vkRequest.getObject().getFrom_id()).message(text).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
