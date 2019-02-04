package com.max.example.files.services;

import com.max.example.files.datanodes.classes.Homework;
import com.max.example.files.datanodes.repositories.HomeworkRepository;
import com.max.example.files.entities.VKGroupMessage;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HomeworkAttentionSystem implements CommandLineRunner {


    @Autowired
    private HomeworkRepository homeworkRepository;

    private TransportClient transportClient;
    private VkApiClient vk;
    private GroupActor actor;


    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public void main(String[] args) {
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        actor = new GroupActor(177305058, "6afde058b95ce78f27ce1ee66fabc3d66adf81e66d154879c8b57a919e8697580989a30fe9f165896244e");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Iterable<Homework> hwIterable = homeworkRepository.findAll();
                    Date date = new Date();
                    System.out.println("Поиск ДЗ");
                    for(Homework hw: hwIterable){
                        System.out.println();
                        if(hw.getRemindDate()>=date.getTime()){
                            try {
                                vk.messages().send(actor).userId(hw.getOwnerId()).message("Напоминание о домашнем задании:\n" +
                                        hw.getTaskText()).execute();
                            } catch (ApiException e) {
                                e.printStackTrace();
                            } catch (ClientException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep(900000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
