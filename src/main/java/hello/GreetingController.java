package hello;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.catalina.servlet4preview.http.ServletMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.NoSuchElementException;

@Controller
public class GreetingController {

    @GetMapping(value = "/greeting", produces = "application/json")
    public @ResponseBody
    Object getGreeting(@RequestParam(required = true, name = "name") String name, Map<String, Object> model) {
//        model.put("name", name);
//        Object[] models = {model};
//
//
//        return models;
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson json = gsonBuilder.create();
        ServerNode s = new ServerNode(name);
        return json.toJson(s);
    }


    @GetMapping()
    public String main() {

        return "main";
    }

    @PostMapping()
    public @ResponseBody
    Object confirmAnswer(HttpServletRequest request) {
//        if(type.equals("confirmation")&&groupId==177305058 ){
//            return "bd646e13";
//        }


        Map<String, String[]> paarams = request.getParameterMap();
        for (Map.Entry<String, String[]> pem : paarams.entrySet()) {
//
//            System.out.print(pem.getKey()+" : ");
//            for(String s:pem.getValue()){
//                System.out.println(s);
//            }
//        }
//        //System.out.println(request.getParameterNames());
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson json = gsonBuilder.create();

        }
        return "ks";
    }
}
