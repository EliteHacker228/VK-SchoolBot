package hello;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class GreetingController {

    @GetMapping(value = "/greeting", produces = "application/json")
    public @ResponseBody Object getGreeting(@RequestParam(required = true, name="name") String name, Map<String, Object> model){
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
    public @ResponseBody Object confirmAnswer(@RequestParam(required = false, name="type") String type,
                        @RequestParam(required = false, name="group_id") Integer groupId){
        if(type.equals("confirmation")&&groupId==177305058 ){
            return "bd646e13";
        }
        return "Nothing";
    }
}
