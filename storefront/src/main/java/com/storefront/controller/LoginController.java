package com.storefront.controller;

import com.storefront.data.LoginData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Optional;

@Controller
public class LoginController {

    @Resource(name = "simpleClienRestTemplate")
    private RestTemplate restTemplate;

    @GetMapping("/login/{id}")
    public String login(@PathVariable(name = "id") Optional<String> uid,
                        final Model model) {
        if (uid.isPresent())
        {
            final String url = String.format("http://localhost:8000/api/login/%s", uid.get());
            final ResponseEntity<LoginData> loginData = restTemplate.getForObject(url, ResponseEntity.class);
            model.addAttribute("failureReason", loginData.getBody().getFailureReason());
        }
        return "login";
    }
}
