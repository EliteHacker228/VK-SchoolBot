package hello;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.VKRequest;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.catalina.servlet4preview.http.ServletMapping;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import services.MessageService;

import java.util.Map;
import java.util.NoSuchElementException;

@Controller
public class GreetingController {

//    @GetMapping(value = "/greeting", produces = "application/json")
//    public @ResponseBody
//    Object getGreeting(@RequestParam(required = true, name = "name") String name, Map<String, Object> model) {
////        model.put("name", name);
////        Object[] models = {model};
////
////
////        return models;
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson json = gsonBuilder.create();
//        ServerNode s = new ServerNode(name);
//        return json.toJson(s);
//    }


//    @GetMapping()
//    public String main() {
//
//        return "main";
//    }

    @PostMapping(consumes = "application/json")
    public @ResponseBody
    Object confirmAnswer(@RequestBody VKRequest vkRequest) {
//        if(type.equals("confirmation")&&groupId==177305058 ){
//            return "bd646e13";
//        }

        //System.out.println(str);

//        Map<String, String[]> paarams = request.getParameterMap();
//        for (Map.Entry<String, String[]> pem : paarams.entrySet()) {
//
//            System.out.print(pem.getKey()+" : ");
//            for(String s:pem.getValue()){
//                System.out.println(s);
//            }
//        }
        //System.out.println(request.getParameterNames());
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson json = gsonBuilder.create();
//        System.out.println(str);

        //System.out.println(vkRequest.getObject().getText());
        System.out.println(vkRequest.getObject().getText());

        GsonBuilder gsonBuilderson = new GsonBuilder();
        Gson json = gsonBuilderson.create();

        System.out.println(json.toJson(vkRequest));
        System.out.println(json.toJson(vkRequest.getObject()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageService ms = new MessageService(vkRequest);
                ms.sendMessage("Другое сообщение");
            }
        }).start();

        return "ok";
    }
}
