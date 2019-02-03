package com.max.example.files.services;

import com.max.example.files.datanodes.classes.Region;
import com.max.example.files.datanodes.classes.SClass;
import com.max.example.files.datanodes.classes.School;
import com.max.example.files.datanodes.classes.Student;
import com.max.example.files.datanodes.repositories.ClassesRepository;
import com.max.example.files.datanodes.repositories.RegionsRepository;
import com.max.example.files.datanodes.repositories.SchoolsRepository;
import com.max.example.files.datanodes.repositories.StudentsRepository;
import com.max.example.files.entities.StudentStatus;
import com.max.example.files.entities.StudentsRoles;
import com.max.example.files.entities.VKGroupMessage;
import com.max.example.files.entities.VKRequest;
import com.vk.api.sdk.actions.Users;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import com.vk.api.sdk.queries.users.UsersGetQuery;
import com.vk.api.sdk.queries.users.UsersNameCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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
            Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
//            UsersGetQuery ugq = vk.users().get(new UserActor(168148426,"6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e"));
//            ArrayList<UserXtrCounters> ugqMap = null;
//            try {
//                ugqMap = (ArrayList<UserXtrCounters>) ugq.userIds("168148426").fields().nameCase(UsersNameCase.NOMINATIVE).execute();
//            } catch (ApiException e) {
//                e.printStackTrace();
//            } catch (ClientException e) {
//                e.printStackTrace();
//            }
//            UserXtrCounters userXtrCounters = ugqMap.get(0);
//            System.out.println(userXtrCounters.getFirstName());
//            System.out.println(userXtrCounters.getLastName());
            switch(StudentStatus.valueOf(student.getStatus())){
                case STUDENT_REGION_REGISTRATION:
                    studentRegionRegistration();
                    break;

                case STUDENT_SCHOOL_REGISTRATION:
                    studentSchoolRegistration();
                    break;

                case STUDENT_CLASS_REGISTRATION:
                    studentClassRegistration();
                    break;

                case STUDENT_IN_ACTION:
                    queryBrancher();
                    break;

                case STUDENT_CHOOSE:
                    queryRouter();
                    break;

                case STUDENT_CHOOSED_CALCULATOR:
                    String result = "";
                    MarkCalculator mk = new MarkCalculator(vkGroupMessage.getText());
                    ArrayList<String> marklist = mk.workMethod();
                    for(String mstr: marklist){
                        result+=mstr+'\n';
                    }
                    sendMessage(result);
                    student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                    studentsRepository.save(student);
            }
//            if (student.getRegionId() == null) {
//                studentRegionRegistration();
//            }
//
//            if (student.getSchoolId() == null) {
//                studentSchoolRegistration();
//            }
//
//            if (student.getClassId() == null) {
//                studentClassRegistration();
//            }
        }

//        System.out.println("Здравствуйте!");
    }

    private void queryRouter(){
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        String query = "";
        String text = vkGroupMessage.getText();
        if(text.contains(".")) {
            String[] splittedText = text.split(".");
            query = splittedText[0];
        }else if(text.length()==1 && text.matches("[-+]?\\d+")){
            query=text;
        }else{
            sendMessage("Неверная команда");//STUDENT_IN_ACTION
            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());

        }


        switch (Integer.parseInt(query)){
            case 1:
                sendMessage("Извините, функци записи ДЗ пока не работает :(");
                break;
            case 2:
                student.setStatus(StudentStatus.STUDENT_CHOOSED_CALCULATOR.name());
                studentsRepository.save(student);
                sendMessage("Калькулятор оценок Школобота v1.0. \n" +
                        "Инструкция: \n" +
                        "1. Введите ваши оценки в одну строку без пробелов и других символов(например: 554354445)\n" +
                        "2. Получите результат.\n" +
                        "Примечание: калькулятор действует только для пятибалльной шкалы оценивания");
                break;

            default:
                sendMessage("Извините, такой команды нет");
            break;
        }
    }

    private void queryBrancher(){
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        UsersGetQuery ugq = vk.users().get(new UserActor(vkGroupMessage.getFrom_id(),"6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e"));
        ArrayList<UserXtrCounters> ugqMap = null;
        try {
            ugqMap = (ArrayList<UserXtrCounters>) ugq.userIds("168148426").fields().nameCase(UsersNameCase.NOMINATIVE).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        UserXtrCounters userXtrCounters = ugqMap.get(0);
        sendMessage("Здравствуйте, "+ userXtrCounters.getFirstName()+"! Чего желаете?\n"+
        "\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\n" +
                "\uD83D\uDCDA1. Записать ДЗ\n" +
                "\uD83D\uDCC82. Калькулятор оценок\n" +
        "\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11\uD83D\uDD11");
        student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
        studentsRepository.save(student);
    }

    private void studentServiceClac(){
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        String text = vkGroupMessage.getText();

        MarkCalculator mk = new MarkCalculator(text.trim());
        mk.workMethod();

        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
        studentsRepository.save(student);

    }

    private void studentRegistration() {
        Student student = new Student();
        student.setVkId(vkGroupMessage.getFrom_id());
        student.setStatus(StudentStatus.STUDENT_REGION_REGISTRATION.name());
        studentsRepository.save(student);

        sendMessage("Привет! Ты пока не зарегистрирован в нашей системе!" +
                "Для того, чтобы зарегистрироваться, ответь на несколько вопросов.");

        sendMessage("Какой твой регион?");
        int counter = 1;

        String regions = "";
        for (Region region : regionsRepository.findAll()) {
            regions += String.format("%d. " + region.getName(), counter) + "\n";
            counter++;
        }
        sendMessage(regions);
    }

    private void studentRegionRegistration() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);

        if (student.getRegionId() == null) {
            if (vkGroupMessage.getText().matches("[-+]?\\d+")
                    && Integer.parseInt(vkGroupMessage.getText()) <=
                    makeCollection(regionsRepository.findAll()).size()) {
                student.setRegionId(Integer.parseInt(vkGroupMessage.getText()));
                studentsRepository.save(student);
                sendMessage("Регион записан!");
                System.out.println();
            } else {
                sendMessage("Ошибка! Такого региона нет.");
                System.out.println();
                return;
            }

        }

        sendMessage("Из какой ты школы?");
        student.setStatus(StudentStatus.STUDENT_SCHOOL_REGISTRATION.name());
        studentsRepository.save(student);

        String schools = "";
        int counter = 1;
        for (School school : schoolsRepository.findAll()) {
            schools += String.format("%d. " + school.getName(), counter) + "\n";
            counter++;
        }
        sendMessage(schools);
    }

    private void studentSchoolRegistration() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        if (student.getSchoolId() == null) {
            if (vkGroupMessage.getText().matches("[-+]?\\d+")
                    && Integer.parseInt(vkGroupMessage.getText()) <=
                    makeCollection(regionsRepository.findAll()).size()) {
                student.setSchoolId(Integer.parseInt(vkGroupMessage.getText()));
                studentsRepository.save(student);
                sendMessage("Школа записана!");
            } else {
                sendMessage("Ошибка! Такой школы нет.");
                return;
            }

        }

        sendMessage("Введи свой класс (например, 7Б, 10А и т.д)");
        student.setStatus(StudentStatus.STUDENT_CLASS_REGISTRATION.name());
        studentsRepository.save(student);


        return;
    }

    private void studentClassRegistration() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        //System.out.println("Ввод класса");

        if (student.getClassId() == null) {
            //System.out.println("Class id: " + student.getClassId());
            String[] classNode;
            try {
                classNode = stringToClass(vkGroupMessage.getText());
                //System.out.println("Message: " + vkGroupMessage.getText());


                for (SClass sClass : classesRepository.findBySchoolId(student.getSchoolId())) {
                    System.out.println(sClass);
                    if (sClass.getNumber() == Integer.parseInt(classNode[0])
                            && sClass.getLetter().equals(classNode[1])
                            && sClass.getSchoolId().equals(student.getSchoolId())
                            ) {

                        student.setClassId(sClass.getId());
                        studentsRepository.save(student);
                        System.out.println("Готово!");
                    }
                }

            } catch (Exception e) {
                sendMessage("Такого класса нет.");
                return;
            }
        }
        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
        studentsRepository.save(student);


        sendMessage("Здравствуйте!");

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
