package com.max.example.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.max.example.files.datanodes.classes.Region;
import com.max.example.files.datanodes.classes.SClass;
import com.max.example.files.datanodes.classes.School;
import com.max.example.files.datanodes.classes.Student;
import com.max.example.files.datanodes.repositories.*;
import com.max.example.files.entities.VKGroupMessage;
import com.max.example.files.entities.VKRequest;
import com.max.example.files.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private PrivateKeysRepository privateKeysRepository;

    @Autowired
    private SchoolScheduleRepository schoolScheduleRepository;


    @PostMapping
    private @ResponseBody String messageGetter(@RequestBody VKRequest vkRequest){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson json = gsonBuilder.create();

                System.out.println(json.toJson(vkRequest));
                System.out.println("=================");
                System.out.println(json.toJson(vkRequest.getObject()));

                VKGroupMessage vkGroupMessage=vkRequest.getObject();
                MessageService ms = new MessageService(vkRequest, regionsRepository, classesRepository,
                        schoolsRepository, studentsRepository, homeworkRepository, privateKeysRepository,
                        schoolScheduleRepository);
                ms.workMethod();
            }
        }).start();


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
