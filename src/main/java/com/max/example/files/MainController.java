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
        vkGroupMessage=vkRequest.getObject();
        MessageService ms = new MessageService(vkRequest);

        Collection<Student> students = makeCollection(studentsRepository.findAll());

        if(studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty()){
            //System.out.println("empty");

            Student student = new Student();
            student.setVkId(vkGroupMessage.getFrom_id());
            studentsRepository.save(student);

            ms.sendMessage("Привет! Ты пока не зарегистрирован в нашей системе!" +
                    "Для того, чтобы зарегистрироваться, ответь на несколько вопросов.");

            ms.sendMessage("Какой твой регион?");
            int counter=1;

            String regions = "";
            for( Region region: regionsRepository.findAll()){
                regions+=String.format("%d. "+region.getName(), counter)+"\n";
                counter++;
            }
            ms.sendMessage(regions);


            return "ok";
        }

        if(!studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty() &&
                studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getRegionId()==null){
            Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);

            if(student.getRegionId()==null){
                if(vkGroupMessage.getText().matches("[-+]?\\d+")
                        && Integer.parseInt(vkGroupMessage.getText())<=
                        makeCollection(regionsRepository.findAll()).size()){
                    student.setRegionId(Integer.parseInt(vkGroupMessage.getText()));
                    studentsRepository.save(student);
                    ms.sendMessage("Регион записан!");
                    System.out.println();
                }else{
                    ms.sendMessage("Ошибка! Такого региона нет.");
                    System.out.println();
                    return "ok";
                }

            }

            ms.sendMessage("Из какой ты школы?");
            System.out.println();

            String schools = "";
            int counter=1;
            for( School school: schoolsRepository.findAll()){
                schools+=String.format("%d. "+school.getName(), counter)+"\n";
                counter++;
            }
            ms.sendMessage(schools);


            return "ok";

        }

        if(!studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty() &&
                studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getSchoolId()==null){
            Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);

            if(student.getSchoolId()==null){
                if(vkGroupMessage.getText().matches("[-+]?\\d+")
                        && Integer.parseInt(vkGroupMessage.getText())<=
                        makeCollection(regionsRepository.findAll()).size()){
                    student.setSchoolId(Integer.parseInt(vkGroupMessage.getText()));
                    studentsRepository.save(student);
                    ms.sendMessage("Школа записана!");
                }else{
                    ms.sendMessage("Ошибка! Такой школы нет.");
                    return "ok";
                }

            }

            ms.sendMessage("Введи свой класс (например, 7Б, 10А и т.д)");

            return "ok";

        }

        if(!studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).isEmpty() &&
                studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getClassId()==null){
            Student student = studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0);
            System.out.println("Ввод класса");

            if(student.getClassId()==null){
                System.out.println("Class id: "+student.getClassId());
                String[] classNode;
                try {
                    classNode = stringToClass(vkGroupMessage.getText());
                    System.out.println("Message: "+vkGroupMessage.getText());


                    for(SClass sClass: classesRepository.findBySchoolId(student.getSchoolId())){
                        System.out.println(sClass);
                        if(     sClass.getNumber()==Integer.parseInt(classNode[0])
                                && sClass.getLetter().equals(classNode[1])
                                && sClass.getSchoolId().equals(student.getSchoolId())
                                ){

                            student.setClassId(sClass.getId());
                            studentsRepository.save(student);
                            ms.sendMessage("Готово! ");
                        }
                    }

                } catch (Exception e) {
                    ms.sendMessage("Такого класса нет.");
                    return "ok";
                }
            }
            //System.out.println(studentsRepository.findByVkId(vkGroupMessage.getFrom_id()).get(0).getClassId()==null);
            ms.sendMessage("Здравствуйте!");

            return "ok";

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
