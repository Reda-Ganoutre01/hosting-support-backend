package hosting_support_backend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/hey")
    public String sayHey(){
        return "hey";
    }
}
