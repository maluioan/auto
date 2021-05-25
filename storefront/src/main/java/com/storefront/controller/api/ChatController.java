package com.storefront.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @GetMapping("/chat/{username}")
    public String getChatsForUser(@PathVariable("username") String username) {
        return "chats for " + username;
    }
}
