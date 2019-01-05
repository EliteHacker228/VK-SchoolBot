package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class GreetingController {

    @PostMapping
    public String add(@RequestParam String name, Map<String, Object> model) {
        model.put("name", name);
        System.out.println("Получен POST запрос со значением: "+name);
        return "greeting";
    }

    @GetMapping
    public String main() {

        return "main";
    }
}
