package com.max.example.files;

import com.max.example.files.datanodes.classes.Region;
import com.max.example.files.datanodes.classes.SClass;
import com.max.example.files.datanodes.classes.School;
import com.max.example.files.datanodes.classes.Student;
import com.max.example.files.datanodes.repositories.ClassesRepository;
import com.max.example.files.datanodes.repositories.RegionsRepository;
import com.max.example.files.datanodes.repositories.SchoolsRepository;
import com.max.example.files.datanodes.repositories.StudentsRepository;
import com.max.example.files.entities.VKGroupMessage;
import com.max.example.files.entities.VKRequest;
import com.max.example.files.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;

@Controller
public class MainController {

    @Autowired
    private RegionsRepository regionsRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SchoolsRepository schoolsRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @PostMapping
    private @ResponseBody String messageGetter(@RequestBody VKRequest vkRequest, VKGroupMessage vkGroupMessage){
//        vkGroupMessage=vkRequest.getObject();
//        MessageService ms = new MessageService(vkRequest);
//
//        Collection<Student> students = makeCollection(studentsRepository.findAll());
//        if(studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty()){
//            System.out.println("empty");
//
//            Student student = new Student();
//            student.setVkId(vkGroupMessage.getFrom_id());
//            studentsRepository.save(student);
//
//            System.out.println("Привет! Ты пока не зарегистрирован в нашей системе!" +
//                    "Для того, чтобы зарегистрироваться, ответь на несколько вопросов.");
//            System.out.println();
//            System.out.println("Какой твой регион?");
//            int counter=1;
//
//            for( Region region: regionsRepository.findAll()){
//                System.out.format("%d. "+region.getName(), counter);
//                counter++;
//            }
//            System.out.println();
//
//
//            return "ok";
//        }
//
//        if(!studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty() &&
//                studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getRegionId()==null){
//            Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
//
//            if(student.getRegionId()==null){
//                if(vkGroupMessage.getText().matches("[-+]?\\d+")
//                        && Integer.parseInt(vkGroupMessage.getText())<=
//                        makeCollection(regionsRepository.findAll()).size()){
//                    student.setRegionId(Integer.parseInt(vkGroupMessage.getText()));
//                    studentsRepository.save(student);
//                    System.out.println("Регион записан!");
//                    System.out.println();
//                }else{
//                    System.out.println("Ошибка! Такого региона нет.");
//                    System.out.println();
//                }
//
//            }
//
//            System.out.println("Из какой ты школы?");
//            System.out.println();
//
//            int counter=1;
//            for( School school: schoolsRepository.findAll()){
//                System.out.format("%d. "+school.getName(), counter);
//                counter++;
//            }
//            System.out.println();
//
//
//            return "ok";
//
//        }
//
//        if(!studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty() &&
//                studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getSchoolId()==null){
//            Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
//
//            if(student.getSchoolId()==null){
//                if(vkGroupMessage.getText().matches("[-+]?\\d+")
//                        && Integer.parseInt(vkGroupMessage.getText())<=
//                        makeCollection(regionsRepository.findAll()).size()){
//                    student.setSchoolId(Integer.parseInt(vkGroupMessage.getText()));
//                    studentsRepository.save(student);
//                    System.out.println("Школа записана!");
//                }else{
//                    System.out.println("Ошибка! Такой школы нет.");
//                }
//
//            }
//
//            System.out.println("Из какого ты класса?");
//
//            return "ok";
//
//        }
//
//        if(!studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty() &&
//                studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getClassId()==null){
//            Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
//            System.out.println("Ввод класса");
//
//            if(student.getClassId()==null){
//                System.out.println("Class id: "+student.getClassId());
//                String[] classNode;
//                try {
//                    classNode = stringToClass(vkGroupMessage.getText());
//                    System.out.println("Message: "+vkGroupMessage.getText());
//
//
//                    for(SClass sClass: classesRepository.findBySchoolId(student.getSchoolId())){
//                        System.out.println(sClass);
//                        if(     sClass.getNumber()==Integer.parseInt(classNode[0])
//                                && sClass.getLetter().equals(classNode[1])
//                                && sClass.getSchoolId().equals(student.getSchoolId())
//                                ){
//
//                            student.setClassId(sClass.getId());
//                            studentsRepository.save(student);
//                            System.out.println("Finded");
//                        }
//                    }
//
//                } catch (Exception e) {
//                    System.out.println("Такого класса нет.");
//                    return "ok";
//                }
//            }
//            //System.out.println(studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getClassId()==null);
//            System.out.println("Готово!");
//
//            return "ok";
//
//        }

//        Region region = new Region();
//        region.setId(1);
//        region.setName("Свердловская область");
//        regionsRepository.save(region);
//
//        School school = new School();
//        school.setId(1);
//        school.setName("МАОУ СОШ №67 с УИОП");
//        school.setRegionId(1);
//        schoolsRepository.save(school);


        SClass sClass = new SClass();
        int counter = 1;
        for(int i = 10; i>=1; i--){
            for(char c = 'А'; c<='Г'; c++){
               sClass.setId(counter);
               sClass.setNumber(i);
               sClass.setLetter(String.valueOf(c));
               sClass.setSchoolId(1);
               counter++;
               System.out.println(sClass);
               classesRepository.save(sClass);
            }
        }



        return "ok";
    }

    public String[] stringToClass(String str) throws Exception {

        str=str.toUpperCase();
        if(str.matches("[-+]?\\d+")){
            throw new Exception("Wrong class");
        }
        if(str.length()>=2 && str.length()<=3) {
            String[] result = {str.substring(0, str.length() - 1),
                    str.substring(str.length() - 1, str.length())
            };
            return result;
        }else{
            throw new Exception("Wrong class");
        }
    }

    public <T> Collection<T> makeCollection(Iterable<T> iter) {
        Collection<T> list = new ArrayList<T>();
        for (T item : iter) {
            list.add(item);
        }
        return list;
    }
}
