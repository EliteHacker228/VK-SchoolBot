package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String getGreeting(@RequestParam(required = true, name="name") String name, Map<String, Object> model){
        model.put("name", name);

        return "greeting";
    }


    @GetMapping
    public String main() {

        return "main";
    }
}
