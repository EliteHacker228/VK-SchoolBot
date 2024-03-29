package com.max.example.files.services;

import com.max.example.files.datanodes.classes.*;
import com.max.example.files.datanodes.repositories.*;
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
import org.hibernate.jdbc.Expectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private HomeworkRepository homeworkRepository;
    private PrivateKeysRepository privateKeysRepository;
    private SchoolScheduleRepository schoolScheduleRepository;

    /**
     * 📚1. Записать ДЗ
     * 📗2. Просмотреть записанное ДЗ
     * 📈3. Калькулятор оценок
     * 📊4. Просмотреть расписание на неделю
     * ⚠5. Отправить объявление
     * ☀6. Сообщить об изменениях в расписании
     * 💬7. Сообщить о домашнем задании
     * 📝8. Добавить/Редактировать расписание
     * 🔓9. Сгенерировать ключ старосты
     * 🔓10. Сгенерировать ключ учителя
     * 🚫0. Перерегистрировать свой аккаунт
     **/

    private final int ADD_HOMEWORK = 1;
    private final int SHOW_HOMEWORK = 2;
    private final int MARKS_CALCULATOR = 3;

    private final int SHOW_SCHEDULE = 4;
    private final int SEND_ATTENTION = 5;
    private final int ADD_EDIT_SCHEDULE_CHANGES = 6;
    private final int SEND_HOMEWORK_ATTENTION = 7;
    private final int ADD_OR_EDIT_SCHEDULE = 8;
    private final int HEADMAN_KEY_GENERATE = 9;
    private final int TEACHER_KEY_GENERATE = 10;
    private final int RE_REGISTRATION = 0;



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

    public MessageService(VKRequest vkRequest, RegionsRepository regionsRepository,
                          ClassesRepository classesRepository, SchoolsRepository schoolsRepository,
                          StudentsRepository studentsRepository, HomeworkRepository homeworkRepository,
                          PrivateKeysRepository privateKeysRepository, SchoolScheduleRepository schoolScheduleRepository) {

        this.vkRequest = vkRequest;
        this.regionsRepository = regionsRepository;
        this.classesRepository = classesRepository;
        this.schoolsRepository = schoolsRepository;
        this.studentsRepository = studentsRepository;
        this.homeworkRepository = homeworkRepository;
        this.privateKeysRepository = privateKeysRepository;
        this.schoolScheduleRepository = schoolScheduleRepository;

        vkGroupMessage = vkRequest.getObject();

        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        actor = new GroupActor(177305058, "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e");

    }

    private void main(String[] args) {

    }

//    public boolean studentFieldsValidator(Student student){
//        if(student.getRegionId()==null){
//            student.setStatus(StudentStatus.STUDENT_REGION_REGISTRATION.name());
//            studentsRepository.save(student);
//
//        }
//        return false;
//    }

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

            /**
             * TODO: Fix this
             * Fixed
             */
            if ((student.getRegionId() == null || student.getSchoolId() == null || student.getClassId() == null) &&
                    !student.getStatus().contains("REGISTRATION")) {
                //Условие соблюдено => произошла ошибка при первой регистрации
                if (student.getRegionId() == null) {
                    sendMessage("Вы не указали свой регион. Пожалуйста, выберите его: \n");
                    int counter = 1;

                    String regions = "";
                    for (Region region : regionsRepository.findAll()) {
                        regions += String.format("%d. " + region.getName(), counter) + "\n";
                        counter++;
                    }
                    sendMessage(regions);

                    student.setStatus(StudentStatus.STUDENT_REGION_REGISTRATION.name());
                    studentsRepository.save(student);

                    return;

                } else if (student.getSchoolId() == null) {
                    sendMessage("Вы не указали свою школу. Пожалуйста, укажите её номер. \n Если не видите в списке вашу школу - отправьте её официальное название" +
                            "(например, МАОУ СОШ №67 с УИОП), и она зарегистрируется в системе.");

                    String schools = "";
                    int counter = 1;
                    for (School school : schoolsRepository.findAll()) {
                        System.out.println(school.getName() + " : " + school.isVisible());
                        if (school.isVisible()) {
                            System.out.println(school.getName() + " - " + "видима");
                            schools += String.format("%d. " + school.getName(), counter) + "\n";
                            counter++;
                        } else {
                            System.out.println(school.getName() + " - " + "невидима");
                        }
                    }
                    sendMessage(schools);

                    student.setStatus(StudentStatus.STUDENT_SCHOOL_REGISTRATION.name());
                    studentsRepository.save(student);

                    return;

                } else if (student.getClassId() == null) {
                    sendMessage("Вы не указали свой класс. Пожалуйста, укажите его (например, 7Б, 10А и т.д)");
                    student.setStatus(StudentStatus.STUDENT_CLASS_REGISTRATION.name());
                    studentsRepository.save(student);

                    return;
                }
            }
//            if(student.getRegionId()==null
//                    && !student.getStatus().equals(StudentStatus.STUDENT_REGION_REGISTRATION.name())){
//
//                int counter = 1;
//                String msg = "Регион не зарегистрирован! Укажите ваш регион: \n";
//                String regions = "";
//                for (Region region : regionsRepository.findAll()) {
//                    regions += String.format("%d. " + region.getName(), counter) + "\n";
//                    counter++;
//                }
//                sendMessage(msg+regions);
//                student.setStatus(StudentStatus.STUDENT_REGION_REGISTRATION.name());
//                studentsRepository.save(student);
//                return;
//            }else if(student.getSchoolId()==null
//                    && !student.getStatus().equals(StudentStatus.STUDENT_SCHOOL_REGISTRATION.name())){
//
//                sendMessage("Укажи номер своей школы. \n Если не видишь в списке свою школу - отправь её официальное название" +
//                        "(например, МАОУ СОШ №67 с УИОП), и она зарегистрируется в системе.");
//                String schools = "";
//                int counter = 1;
//                for (School school : schoolsRepository.findAll()) {
//                    schools += String.format("%d. " + school.getName(), counter) + "\n";
//                    counter++;
//                }
//                sendMessage(schools);
//
//                student.setStatus(StudentStatus.STUDENT_SCHOOL_REGISTRATION.name());
//                studentsRepository.save(student);
//                return;
//            }else if(student.getClassId()==null
//                    && !student.getStatus().equals(StudentStatus.STUDENT_CLASS_REGISTRATION.name())){
//
//                sendMessage("Введи свой класс (например, 7Б, 10А и т.д)");
//                student.setStatus(StudentStatus.STUDENT_CLASS_REGISTRATION.name());
//                studentsRepository.save(student);
//                return;
//            }

//            else{
//                student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
//                studentsRepository.save(student);
//            }


            switch (StudentStatus.valueOf(student.getStatus())) {
                case STUDENT_REGION_REGISTRATION:
//                    if (privateKeysRepository.findByKey(vkGroupMessage.getText()).size() > 0) {
//                        activateKey(student);
//                        return;
//                    }
                    studentRegionRegistration();
                    break;

                case STUDENT_SCHOOL_REGISTRATION:
//                    if (privateKeysRepository.findByKey(vkGroupMessage.getText()).size() > 0) {
//                        activateKey(student);
//                        return;
//                    }
                    studentSchoolRegistration();
                    break;

                case STUDENT_CLASS_REGISTRATION:
//                    if (privateKeysRepository.findByKey(vkGroupMessage.getText()).size() > 0) {
//                        activateKey(student);
//                        return;
//                    }
                    studentClassRegistration();
                    break;

                case STUDENT_IN_ACTION:
                    queryBrancher();
                    break;

                case STUDENT_CHOOSE:
                    queryRouter();
                    break;

                case STUDENT_CHOOSED_CALCULATOR:
                    studentServiceClac();

                    student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
                    studentsRepository.save(student);
                    queryBrancher();
                    break;

                case STUDENT_CHOSED_ADD_HOMEWORK:
                    studentServiceAddHomework();

                    student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
                    studentsRepository.save(student);
                    queryBrancher();
                    break;

                case STUDENT_CHOSED_SEND_HOMEWORK_ATTENTION:
                    sendHomework();

                    student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
                    studentsRepository.save(student);
                    queryBrancher();
                    break;

                case STUDENT_CHOOSED_SEND_SCHEDULE_CHANGES_ATTENTION:
                    studentAddScheduleChanges();

                    student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
                    studentsRepository.save(student);
                    queryBrancher();
                    break;

                case STUDENT_CHOSED_SEND_ATTENTION:
                    studentSendAttention();

                    student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
                    studentsRepository.save(student);
                    queryBrancher();
                    break;

                case STUDENT_CHOSED_SCHEDULE_NODE:
                    studentAddScheduleNode();

                    student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
                    studentsRepository.save(student);
                    queryBrancher();
                    break;

                case STUDENT_CHOOSED_RE_REGISTRATION: //если 1 - перерегистрация, в противном случае - отмена.
                    studentReRegistration();

                    break;
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

    private void queryRouter() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        String query = "";
        String text = vkGroupMessage.getText();
        if (privateKeysRepository.findByKey(vkGroupMessage.getText()).size() > 0) {
            activateKey(student);
            return;
        }

        //student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
        //            queryBrancher();

        try {
            if (text.contains(".")) {
                String[] splittedText = text.split(".");
                query = splittedText[0];
            } else if (text.matches("[-+]?\\d+")) {
                query = text;
            } else {
                sendMessage("Неверная команда. Отправьте боту любое сообщение, чтобы вызвать меню");//STUDENT_IN_ACTION
                student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                studentsRepository.save(student);
                return;

            }
        } catch (Exception e) {
            sendMessage("Неверная команда.  Отправьте боту любое сообщение, чтобы вызвать меню");//STUDENT_IN_ACTION
            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
            studentsRepository.save(student);
            return;
        } //Проверка команды на валидность


        try {
            switch (Integer.parseInt(query)) {
                case ADD_HOMEWORK: //1
                    sendMessage("Записать ДЗ.\n" +
                            "Инстукрция:\n" +
                            "1. Отправьте боту сообщение вида: \"задание(день сдачи ДЗ-месяц сдачи ДЗ)\"\n" +
                            "Пример:\n" +
                            "Литература(05.02)\n" +
                            "Алгебра(06.02)\n" +
                            "2. Бот напомнит о задании за сутки до указанного вами срока\n" +
                            "\n" +
                            "Для отмены записи отправьте 0.");
                    student.setStatus(StudentStatus.STUDENT_CHOSED_ADD_HOMEWORK.name());
                    studentsRepository.save(student);
                    break;
                case SHOW_HOMEWORK: //2
                    sendMessage("Просмотр всех записанных вами ДЗ");
                    studentShowAllHomework();
//                student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
//                studentsRepository.save(student);
                    break;
                case MARKS_CALCULATOR: //3
                    student.setStatus(StudentStatus.STUDENT_CHOOSED_CALCULATOR.name());
                    studentsRepository.save(student);
                    sendMessage("Калькулятор оценок Школобота v1.0. \n" +
                            "Инструкция: \n" +
                            "1. Введите ваши оценки в одну строку без пробелов и других символов(например: 554354445)\n" +
                            "2. Получите результат.\n" +
                            "Примечание: калькулятор действует только для пятибалльной шкалы оценивания" +
                            "\n" +
                            "Для отмены записи отправьте 0.");
                    break;

                case SHOW_SCHEDULE: //4
                    showScheduleNodes(true);

                    queryBrancher();
                    break;

                case SEND_ATTENTION: //5
                    if (student.getRole().equals(StudentsRoles.TRUSTED_STUDENT.name()) ||
                            student.getRole().equals(StudentsRoles.ADMIN.name()) ||
                            student.getRole().equals(StudentsRoles.MAIN_ADMIN.name())) {

                        sendMessage("Напишите текст обяъвления здесь.\n" +
                                "Текст должен быть вида:\n" +
                                "1.текст объявления(10Б) - отправка обявления 10Б классу\n" +
                                "2.текст объявления(10!) - отправка обявление всей параллели 10х классов\n" +
                                "3.текст объявления(10Б, 9А, 8В) - отправка обявления 10Б, 9А и 8В классам\n" +
                                "4.текст объявления(10-8) - отправка сообщения параллелям с 10 по 8\n" +
                                "5.текст объявления(*) - отправка объявления всем параллелям\n" +
                                "Для отмены отправки напишите 0");
                        student.setStatus(StudentStatus.STUDENT_CHOSED_SEND_ATTENTION.name());
                        studentsRepository.save(student);

                    } else {
                        sendMessage("Извините, такой команды нет");
                        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                        studentsRepository.save(student);
                    }

                    break;

                case ADD_EDIT_SCHEDULE_CHANGES: //6
                    if (student.getRole().equals(StudentsRoles.TRUSTED_STUDENT.name()) ||
                            student.getRole().equals(StudentsRoles.ADMIN.name()) ||
                            student.getRole().equals(StudentsRoles.MAIN_ADMIN.name())) {

                        sendMessage("Для отправки ученикам уведомления о временном изменении расписания используйте эту функцию." +
                                "Изменения удаляются в субботу в 20:00 по времени Екатеринбурга\n\n" +
                                "" +
                                "Введите расписание следующего формата:\n" +
                                "имя_класса;\n" +
                                "день недели:" +
                                "   предмет1, предмет2, предмет3, предмет4\n\n" +
                                "Например:\n" +

                                "10Б;\n" +
                                "Понедельник: Алгебра, Геометрия," +
                                " Русский, Литература," +
                                " Физика, Английский язык\n" +

                                "Вторник: Алгебра, Геометрия," +
                                " Русский, Литература," +
                                " Физика, Английский язык\n" +
                                "\nДля отмены отравьте 0");

                        student.setStatus(StudentStatus.STUDENT_CHOOSED_SEND_SCHEDULE_CHANGES_ATTENTION.name());
                        studentsRepository.save(student);
                    } else {
                        sendMessage("Извините, такой команды нет");
                        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                        studentsRepository.save(student);
                    }
                    break;

                case SEND_HOMEWORK_ATTENTION:
                    //sendMessage("Потом будет готов");
                    if (student.getRole().equals(StudentsRoles.TRUSTED_STUDENT.name()) ||
                            student.getRole().equals(StudentsRoles.ADMIN.name()) ||
                            student.getRole().equals(StudentsRoles.MAIN_ADMIN.name())) {

                        sendMessage("Для отправки ДЗ используйте команды, на примере следующих: \n" +
                                "\n1. Предмет: задание(9-10; 12.05) - отправит задание, которое нужно сделать к 12.05 всем классам с 9 до 10 параллели. \n" +
                                "\n2. Предмет: задание(10!; 12.05) - отправит задание, которое нужно сделать к 12.05 всей параллели 10х классов. \n" +
                                "\n3. Предмет: задание(10Б; 12.05) - отправит задание, которое нужно сделать к 12.05 10Б классу. \n" +
                                "\n4. Предмет: задание(10Б, 9А, 8Г, 11Д; 12.05) - отправит задание, которое нужно сделать к 12.05 всем классам, перечисленным через запятую. \n" +
                                "\n" +
                                "Для отмены отправьте боту 0.");
                        //sendHomework();
                        student.setStatus(StudentStatus.STUDENT_CHOSED_SEND_HOMEWORK_ATTENTION.name());
                        studentsRepository.save(student);
                    } else {
                        sendMessage("Извините, такой команды нет");
                        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                        studentsRepository.save(student);
                    }
                    break;

                case ADD_OR_EDIT_SCHEDULE: //8
                    if (student.getRole().equals(StudentsRoles.TRUSTED_STUDENT.name()) ||
                            student.getRole().equals(StudentsRoles.ADMIN.name()) ||
                            student.getRole().equals(StudentsRoles.MAIN_ADMIN.name())) {

                        sendMessage("Для создания или изменения постоянного расписания используйте эту функцию. \n" +
                                "Введите расписание следующего формата:\n" +
                                "имя_класса;\n" +
                                "день недели:" +
                                "   предмет1, предмет2, предмет3, предмет4\n" +
                                "Например:\n" +

                                "10Б;\n" +
                                "Понедельник: Алгебра, Геометрия," +
                                " Русский, Литература," +
                                " Физика, Английский язык\n" +

                                "Вторник: Алгебра, Геометрия," +
                                " Русский, Литература," +
                                " Физика, Английский язык\n" +
                                "\nДля отмены отравьте 0");

                        student.setStatus(StudentStatus.STUDENT_CHOSED_SCHEDULE_NODE.name());
                        studentsRepository.save(student);

                    } else {
                        sendMessage("Извините, такой команды нет");
                        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                        studentsRepository.save(student);
                    }
                    break;


                case HEADMAN_KEY_GENERATE: //9
                    if (student.getRole().equals(StudentsRoles.ADMIN.name()) ||
                            student.getRole().equals(StudentsRoles.MAIN_ADMIN.name())) {
                        sendMessage("Ключ доступа для старосты(действителен 1 раз): " + studentGetKey(StudentsRoles.TRUSTED_STUDENT));

                        student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
                        studentsRepository.save(student);

                        queryBrancher();
                    } else {
                        sendMessage("Извините, такой команды нет");
                        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                        studentsRepository.save(student);
                    }
                    break;

                /* case 7:
                if(student.getRole().equals(StudentsRoles.TRUSTED_STUDENT.name()) ||
                        student.getRole().equals(StudentsRoles.ADMIN.name()) ||
                        student.getRole().equals(StudentsRoles.MAIN_ADMIN.name())){

                    sendMessage("Введите расписание следующего формата:\n" +
                            "имя_класса;\n" +
                            "день недели:" +
                            "   предмет1, предмет2, предмет3, предмет4\n" +
                            "Например:\n" +

                            "10Б;\n" +
                            "Понедельник: Алгебра, Геометрия," +
                            " Русский, Литература,"+
                            " Физика, Английский язык\n"+

                            "Вторник: Алгебра, Геометрия," +
                            " Русский, Литература,"+
                            " Физика, Английский язык\n" +
                            "\nДля отмены отравьте 0");

                    student.setStatus(StudentStatus.STUDENT_CHOSED_SCHEDULE_NODE.name());
                    studentsRepository.save(student);

                }else{
                    sendMessage("Извините, такой команды нет");
                    student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                    studentsRepository.save(student);
                }
                break;*/


                case TEACHER_KEY_GENERATE: //10
                    if (student.getRole().equals(StudentsRoles.MAIN_ADMIN.name())) {
                        sendMessage("Ключ доступа для учителя(действителен 1 раз): " + studentGetKey(StudentsRoles.ADMIN));

                        student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
                        studentsRepository.save(student);

                        queryBrancher();
                    } else {
                        sendMessage("Извините, такой команды нет");
                        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                        studentsRepository.save(student);
                    }
                    break;

                case RE_REGISTRATION: //0
                    sendMessage("Данная функция удалит следующие данные вашей учётной записи:\n" +
                            "-Регион\n" +
                            "-Школа\n" +
                            "-Класс\n" +
                            "Если вы хотите продолжить, отправьте 1.\n" +
                            "Для отмены отправьте 0.");
                    student.setStatus(StudentStatus.STUDENT_CHOOSED_RE_REGISTRATION.name());
                    studentsRepository.save(student);
                    break;

                default:
                    sendMessage("Извините, такой команды нет");
                    student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                    studentsRepository.save(student);
                    break;
            }
        }catch(Exception e){
            sendMessage("Неверная команда.  Отправьте боту любое сообщение, чтобы вызвать меню");//STUDENT_IN_ACTION
            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
            studentsRepository.save(student);
            return;
        }
    }

    private void queryBrancher() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        UsersGetQuery ugq = vk.users().get(new UserActor(vkGroupMessage.getFrom_id(), "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e"));
        ArrayList<UserXtrCounters> ugqMap = null;
        try {
            ugqMap = (ArrayList<UserXtrCounters>) ugq.userIds(String.valueOf(vkGroupMessage.getFrom_id())).fields().nameCase(UsersNameCase.NOMINATIVE).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        UserXtrCounters userXtrCounters = ugqMap.get(0);
        if (student.getRole().equals(StudentsRoles.TRUSTED_STUDENT.name())) {

            sendMessage("Здравствуйте, " + userXtrCounters.getFirstName() + "! Чего желаете?\n" +
                    "\uD83D\uDCDA1. Записать ДЗ\n" +
                    "\uD83D\uDCD72. Просмотреть записанное ДЗ\n" +
                    "\uD83D\uDCC83. Калькулятор оценок\n" +
                    "\uD83D\uDCCA4. Просмотреть расписание на неделю\n" +
                    "⚠5. Отправить объявление\n" +
                    "☀6. Сообщить об изменениях в расписании\n" +
                    "\uD83D\uDCAC7. Сообщить о домашнем задании\n" +
                    "\uD83D\uDCDD8. Добавить/Редактировать расписание\n" +
                    "\uD83D\uDEAB0. Перерегистрировать свой аккаунт\n");

        } else if (
                student.getRole().equals(StudentsRoles.ADMIN.name())) {
            sendMessage("Здравствуйте, " + userXtrCounters.getFirstName() + "! Чего желаете?\n" +
                    "\uD83D\uDCDA1. Записать ДЗ\n" +
                    "\uD83D\uDCD72. Просмотреть записанное ДЗ\n" +
                    "\uD83D\uDCC83. Калькулятор оценок\n" +
                    "\uD83D\uDCCA4. Просмотреть расписание на неделю\n" +
                    "⚠5. Отправить объявление\n" +
                    "☀6. Сообщить об изменениях в расписании\n" +
                    "\uD83D\uDCAC7. Сообщить о домашнем задании\n" +
                    "\uD83D\uDCDD8. Добавить/Редактировать расписание\n" +
                    "\uD83D\uDD139. Сгенерировать ключ старосты\n" +
                    "\uD83D\uDD1310. Сгенерировать ключ учителя\n" +
                    "\uD83D\uDEAB0. Перерегистрировать свой аккаунт\n");
        } else if (student.getRole().equals(StudentsRoles.MAIN_ADMIN.name())) {
            sendMessage("Здравствуйте, " + userXtrCounters.getFirstName() + "! Чего желаете?\n" +
                    "\uD83D\uDCDA1. Записать ДЗ\n" +
                    "\uD83D\uDCD72. Просмотреть записанное ДЗ\n" +
                    "\uD83D\uDCC83. Калькулятор оценок\n" +
                    "\uD83D\uDCCA4. Просмотреть расписание на неделю\n" +
                    "⚠5. Отправить объявление\n" +
                    "☀6. Сообщить об изменениях в расписании\n" +
                    "\uD83D\uDCAC7. Сообщить о домашнем задании\n" +
                    "\uD83D\uDCDD8. Добавить/Редактировать расписание\n" +
                    "\uD83D\uDD139. Сгенерировать ключ старосты\n" +
                    "\uD83D\uDD1310. Сгенерировать ключ учителя\n" +
                    "\uD83D\uDEAB0. Перерегистрировать свой аккаунт\n");
        } else {
            sendMessage("Здравствуйте, " + userXtrCounters.getFirstName() + "! Чего желаете?\n" +
                    "\uD83D\uDCDA1. Записать ДЗ\n" +
                    "\uD83D\uDCD72. Просмотреть записанное ДЗ\n" +
                    "\uD83D\uDCC83. Калькулятор оценок\n" +
                    "\uD83D\uDCCA4. Просмотреть расписание на неделю\n" +
                    "\uD83D\uDEAB0. Перерегистрировать свой аккаунт\n");
        }

        student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
        studentsRepository.save(student);
    }

    private void studentReRegistration(){
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        String text = vkGroupMessage.getText().replace("\n", "");
        if(text.trim().equals("1")){
            student.setRegionId(null);
            student.setSchoolId(null);
            student.setClassId(null);

            student.setStatus(StudentStatus.STUDENT_REGION_REGISTRATION.name());
            studentsRepository.save(student);
            sendMessage("Команда выполнена! Напишите боту для продолжения работы");
        }else{
            sendMessage("Команда отменена");
            student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
            studentsRepository.save(student);
            queryBrancher();
        }
    }

    private void studentAddScheduleChanges() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        String text = vkGroupMessage.getText().replace("\n", "");
        if (text.replace(" ", "").equals("0")) {
            sendMessage("Отправка отменена");
            return;
        }


        HashSet<Integer> sClassHashSet = new HashSet<>();
        ArrayList<SchoolScheduleNode> schoolScheduleNodes = ScheduleCreatorService.stringToScheduleConverter(text.replace(", ", ",").replace(",", ", "));
        for (SchoolScheduleNode sn : schoolScheduleNodes) {
            sn.setClassName(sn.getClassName().replace(" ", "").replace("-", "").toUpperCase());
            ArrayList<SClass> sClasses = new ArrayList<>(classesRepository.findBySchoolId(student.getSchoolId()));


            for (SClass sClass : sClasses) {
                if (sClass.getLetter().equals(sn.getClassLetter().toUpperCase()) &&
                        sClass.getNumber() == sn.getClassNumber()) {


                    for (SchoolScheduleNode scheduleNode : schoolScheduleRepository.findByClassId(sClass.getId())) {
                        if (scheduleNode.getDay().equals(sn.getDay())) {
                            scheduleNode.setChanges(sn.getLessons());
                            schoolScheduleRepository.save(scheduleNode);

                            if(!sClassHashSet.contains(sClass.getId())){
                                sClassHashSet.add(sClass.getId());
                            }

                        }
                    }

                }

            }
        }

        System.out.println("SCLASS CONTIAMENT:"+sClassHashSet.size());
        for(Integer sClass1: sClassHashSet){
            System.out.println(sClass1);
        }

        for (Integer sClass : sClassHashSet) {
            System.out.println("SCLASS: "+sClass);
            for (Student student1 : studentsRepository.findByClassId(sClass)) {
                System.out.println("Student: "+student1.getVkId());
                try {
                    vk.messages().send(actor).userId(student1.getVkId()).message("☀ Вам поступили новые изменения в расписании! Выберите пункт \"4\" для просмотра").execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        sendMessage("Оповещение об изменениях отправлено.");

    }

    private void showScheduleNodes(boolean showAllSchedule) {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);

        TreeMap<String, Integer> daysOfTheWeek = new TreeMap<>();
        daysOfTheWeek.put("понедельник", 0);
        daysOfTheWeek.put("вторник", 1);
        daysOfTheWeek.put("среда", 2);
        daysOfTheWeek.put("четверг", 3);
        daysOfTheWeek.put("пятница", 4);
        daysOfTheWeek.put("суббота", 5);
        daysOfTheWeek.put("воскресенье", 6);


        if (showAllSchedule) {
            ArrayList<SchoolScheduleNode> scheduleNodes = new ArrayList<>(schoolScheduleRepository.findByClassId(student.getClassId()));
            String answer = "";
            String answerWithChanges = "--------------------------------------------------\n" +
                    "☀Есть изменения на следующие дни:\n";
            boolean changed = false;

            scheduleNodes.sort((o1, o2) -> daysOfTheWeek.get(o1.getDay().toLowerCase()) - daysOfTheWeek.get(o2.getDay().toLowerCase()));

            for (SchoolScheduleNode sn : scheduleNodes) {
                if (sn.getChanges() != null) {
                    answerWithChanges += sn.getFormattedChanges() + "\n";
                    changed = true;
                }
                answer += sn + "\n";
            }

            if (changed) {
                answerWithChanges += "--------------------------------------------------\n";
                answer = answerWithChanges + answer;
            }

            sendMessage(answer);
        }
    }

    private void studentAddScheduleNode() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        String text = vkGroupMessage.getText().replace("\n", "");
        if (text.replace(" ", "").equals("0")) {
            sendMessage("Отправка отменена");
            return;
        }

        ArrayList<SchoolScheduleNode> schoolScheduleNodes = null;
        try {
            schoolScheduleNodes = ScheduleCreatorService.stringToScheduleConverter(text.replace(", ", ",").replace(",", ", "));
        }catch (IllegalArgumentException e){
            sendMessage("Неверный формат команды!");
            return;
        }
        for (SchoolScheduleNode sn : schoolScheduleNodes) {

            //sn.setLessons(sn.getLessons().replace("%", ""));//
            //System.out.println("SN//: "+sn.getLessons());

            sn.setClassName(sn.getClassName().replace(" ", "").replace("-", "").toUpperCase());
            ArrayList<SClass> sClasses = new ArrayList<>(classesRepository.findBySchoolId(student.getSchoolId()));
            for (SClass sClass : sClasses) {
                if (sClass.getLetter().equals(sn.getClassLetter().toUpperCase()) &&
                        sClass.getNumber() == sn.getClassNumber()) {
                    sn.setClassId(sClass.getId());
                    sn.setId(sClass.getId() + " " + sn.getClassName() + " " + sn.getDay());
                }
            }


            schoolScheduleRepository.save(sn);
        }

        sendMessage("Расписание сохранено!");

    }

    private void activateKey(Student student) {
        String text = vkGroupMessage.getText();
        if (privateKeysRepository.findByKey(text).size() > 0) {
            if (student.getRole().equals(StudentsRoles.STUDENT.name())) {
                student.setRole(privateKeysRepository.findByKey(text).get(0).getRole());
                studentsRepository.save(student);
                String msg = "Ключ активирован. Вам выданы дополнительные разрешения уровня ";
                student.setRole(privateKeysRepository.findByKey(text).get(0).getRole());

                switch (privateKeysRepository.findByKey(text).get(0).getRole()) {
                    case "ADMIN": {
                        msg += "учитель.";
                        break;
                    }

                    case "TRUSTED_STUDENT": {
                        msg += "староста.";
                        break;
                    }
                }
                sendMessage(msg);
                privateKeysRepository.delete(privateKeysRepository.findByKey(text).get(0));
            } else {
                sendMessage("У вас уже есть дополнительные разрешения");
            }

            student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
            studentsRepository.save(student);
            queryBrancher();

            return;
        }
    }

    private String studentGetKey(StudentsRoles studentsRole) {
        PrivateKey privateKey = new PrivateKey();
        privateKey.setKey(String.valueOf(System.nanoTime()));
        privateKey.setRole(studentsRole.name());
        privateKeysRepository.save(privateKey);
        return privateKey.getKey();
    }

    private void sendHomework() {
        UsersGetQuery ugq = vk.users().get(new UserActor(vkGroupMessage.getFrom_id(), "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e"));
        ArrayList<UserXtrCounters> ugqMap = null;

        try {
            ugqMap = (ArrayList<UserXtrCounters>) ugq.userIds(String.valueOf(vkGroupMessage.getFrom_id())).fields().nameCase(UsersNameCase.GENITIVE).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);

        if (vkGroupMessage.getText().trim().equals("0")) {
            sendMessage("Отправка отменена");
            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
            studentsRepository.save(student);
            return;
        }

        UserXtrCounters userXtrCounters = ugqMap.get(0);
//        String attention = "\uD83D\uDCAC Вам потсупило новое домашнее задание от ^id"+student.getVkId()+"["+userXtrCounters.getFirstName()+" "+userXtrCounters.getLastName()+"] "+":\n"+vkGroupMessage.getText();
//        AttentionService as = new AttentionService(attention, vkRequest, studentsRepository, classesRepository);
//        as.workMethod();
        AddHomeworkSystem as = new AddHomeworkSystem(vkGroupMessage.getText(), vkRequest, studentsRepository, classesRepository, homeworkRepository);
        as.workMethod();
        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
        studentsRepository.save(student);
    }

    private void studentSendAttention() {
        UsersGetQuery ugq = vk.users().get(new UserActor(vkGroupMessage.getFrom_id(), "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e"));
        ArrayList<UserXtrCounters> ugqMap = null;

        try {
            ugqMap = (ArrayList<UserXtrCounters>) ugq.userIds(String.valueOf(vkGroupMessage.getFrom_id())).fields().nameCase(UsersNameCase.GENITIVE).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);

        if (vkGroupMessage.getText().trim().equals("0")) {
            sendMessage("Отправка отменена");
            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
            studentsRepository.save(student);
            return;
        }

        UserXtrCounters userXtrCounters = ugqMap.get(0);
        String attention = "⚠ Вам поступило объявление от ^id" + student.getVkId() + "[" + userXtrCounters.getFirstName() + " " + userXtrCounters.getLastName() + "] " + ":\n" + vkGroupMessage.getText();
        AttentionService as = new AttentionService(attention, vkRequest, studentsRepository, classesRepository);
        as.workMethod();
        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
        studentsRepository.save(student);

    }

    private void studentShowAllHomework() {

        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        List<Homework> homeworkList = homeworkRepository.findByOwnerId(student.getVkId());
        String message = "";
        int count = 1;
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM");
        SimpleDateFormat getRussianDayName = new SimpleDateFormat("EEEE", new Locale("ru", "RU"));
        for (Homework hw : homeworkList) {
            Date date = new Date(hw.getDate());
            message += String.valueOf(count) + ". ("+getRussianDayName.format(date).substring(0, 1).toUpperCase()+getRussianDayName.format(date).substring(1)+", "+ sf.format(date) + ")\n " + hw.getTaskText() + "\n";
            count++;

        }

        sendMessage(message);


//        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
//        studentsRepository.save(student);

        student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
        studentsRepository.save(student);
        queryBrancher();

    }

    private void studentServiceAddHomework() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        String text = vkGroupMessage.getText();
//        String textDate;
//        Date parsingdate = null;
//        Date reminddate = null;
//        if(text.contains("(") && text.indexOf("(")==text.indexOf(")")-6){
//            Date date = new Date();
//            SimpleDateFormat format = new SimpleDateFormat("yyyy");
//            textDate = text.substring(text.indexOf("(")+1, text.indexOf(")"))+"."+format.format(date);
//            //Дата сдачи ДЗ
//
//
//            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
//            try {
//                parsingdate=ft.parse(textDate);
//            } catch (ParseException e) {
//                sendMessage("Неверный формат даты");
//                student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
//                studentsRepository.save(student);
//                return;
//            }
//            reminddate=new Date(parsingdate.getTime()-86400000L);
//        }else{
//            sendMessage("Неверный формат даты");
//            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
//            studentsRepository.save(student);
//            return;
//        }
//
//        Homework homework = new Homework();
//
//        homework.setDate(parsingdate.getTime());
//        homework.setRemindDate(reminddate.getTime());
//        homework.setOwnerId(vkGroupMessage.getFrom_id());
//        homework.setTaskText(text.substring(0, text.indexOf("(")));
//        homeworkRepository.save(homework);
//
//        student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
//        studentsRepository.save(student);
//
//        sendMessage("Задание записано!");
        if (text.trim().equals("0")) {
            sendMessage("Запись отменена!");
            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
            studentsRepository.save(student);
            return;
        }
        Pattern pattern = Pattern.compile("[(]\\d\\d[.]\\d\\d[)]");
        Matcher m = pattern.matcher(text);

        String[] splitted = text.split(pattern.toString());

        for (String s : splitted) {
            String taskText = s.trim();
            //System.out.println(taskText);

            if (m.find()) {
                String date = m.group().trim();
                String splittedDate[] = date.split("[()]");
                date = splittedDate[1];
                Date dateDate = new Date();
                date += "." + new GregorianCalendar().get(Calendar.YEAR);
                System.out.println(date);
                SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
                Date newDate = null;
                try {
                    newDate = ft.parse(date);
                    //System.out.println(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Homework hw = new Homework();
                hw.setTaskText(taskText);
                hw.setDate(newDate.getTime());
                hw.setRemindDate(newDate.getTime() - 86400000L);
                hw.setOwnerId(student.getVkId());
                homeworkRepository.save(hw);

                student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                studentsRepository.save(student);

            } else {
                sendMessage("Неверный формат даты!");
                student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
                studentsRepository.save(student);
                return;
            }
        }
        sendMessage("Задание записано!");


    }

    private void studentServiceClac() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        String result = "";
        if (vkGroupMessage.getText().trim().equals("0")) {
            sendMessage("Ввод отменён");
            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
            studentsRepository.save(student);
            return;
        }else if(!(vkGroupMessage.getText().trim().matches("[-+]?\\d+"))){
            sendMessage("Неверный формат ввода");
            student.setStatus(StudentStatus.STUDENT_IN_ACTION.name());
            studentsRepository.save(student);
            return;
        }
        MarkCalculator mk = new MarkCalculator(vkGroupMessage.getText());
        ArrayList<String> marklist = mk.workMethod();
        for (String mstr : marklist) {
            result += mstr + '\n';
        }
        sendMessage(result);
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

        sendMessage("Какой твой регион? Укажи его номер в списке");
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
                int counter = 1;
                String msg = "Регион не зарегистрирован! Укажите ваш регион: \n";
                String regions = "";
                for (Region region : regionsRepository.findAll()) {
                    regions += String.format("%d. " + region.getName(), counter) + "\n";
                    counter++;
                }
                sendMessage(msg + regions);
                System.out.println();
                return;
            }

        }

        if (student.getSchoolId() != null) {
            queryBrancher();
            student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
            studentsRepository.save(student);
            return;
        }

        if (student.getRole().equals(StudentsRoles.STUDENT)) {
            sendMessage("Укажи номер своей школы.");
        } else {
            sendMessage("Укажи номер своей школы. \n Если не видишь в списке свою школу - отправь её официальное название" +
                    "(например, МАОУ СОШ №67 с УИОП), и она зарегистрируется в системе.");
        }

        student.setStatus(StudentStatus.STUDENT_SCHOOL_REGISTRATION.name());
        studentsRepository.save(student);

        String schools = "";
        int counter = 1;
        for (School school : schoolsRepository.findAll()) {
            System.out.println(school.getName() + " : " + school.isVisible());
            if (school.isVisible()) {
                System.out.println(school.getName() + " - " + "видима");
                schools += String.format("%d. " + school.getName(), counter) + "\n";
                counter++;
            } else {
                System.out.println(school.getName() + " - " + "невидима");
            }
        }
        sendMessage(schools);
    }

    private void studentSchoolRegistration() {
        Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
        if (student.getSchoolId() == null) {
            if (vkGroupMessage.getText().matches("[-+]?\\d+")
                    && Integer.parseInt(vkGroupMessage.getText()) <=
                    makeCollection(schoolsRepository.findAll()).size()) {
                student.setSchoolId(Integer.parseInt(vkGroupMessage.getText()));
                studentsRepository.save(student);
                sendMessage("Школа записана!");


            } else if (!student.getRole().equals(StudentsRoles.STUDENT) && vkGroupMessage.getText().contains("№")) {
                School school = new School();
                school.setName(vkGroupMessage.getText().trim());
                school.setRegionId(student.getRegionId());
                school.setVisible(false);
                school.setLowerCaseName();

                if (schoolsRepository.findByLowerCaseName(school.getName().toLowerCase()).size() > 0) {
                    student.setSchoolId(schoolsRepository.findByName(school.getName()).get(0).getId());
                    sendMessage("Школа зарегистрирована! Вы - ученик/учитель " + school.getName());
                } else {
                    schoolsRepository.save(school);

                    int schoolId = school.getId();


                    for (int i = 1; i <= 11; i++) {
                        for (char c = 'А'; c <= 'Д'; c++) {
//                        System.out.println(i+" "+c);
                            SClass sClass = new SClass();

                            sClass.setSchoolId(schoolId);
                            sClass.setNumber(i);
                            sClass.setLetter(String.valueOf(c));
                            classesRepository.save(sClass);
                        }
                    }
                    student.setSchoolId(schoolId);
                    //studentsRepository.save(student);
                    sendMessage("Школа зарегистрирована! Вы - ученик/учитель " + school.getName());

                }

            } else {
                sendMessage("Ошибка! Неверно указана школа.");
                return;
            }

        }

        if (student.getClassId() != null) {
            queryBrancher();
            student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
            studentsRepository.save(student);
            return;
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

        student.setStatus(StudentStatus.STUDENT_CHOOSE.name());
        studentsRepository.save(student);
        queryBrancher();

    }

    private void studentRoleRegistration() {
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
        str = str.replace(" ", "");
        str = str.replace("-", "");

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

    private void sendMessage(String text) {
        try {
            vk.messages().send(actor).userId(vkRequest.getObject().getFrom_id()).message(text).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
