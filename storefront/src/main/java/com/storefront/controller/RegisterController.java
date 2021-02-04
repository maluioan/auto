package com.storefront.controller;

import com.home.automation.users.dto.ContactData;
import com.home.automation.users.dto.RoleData;
import com.home.automation.users.dto.UserData;
import com.storefront.controller.form.RegisterUserForm;
import com.storefront.register.CreateUserDataResultData;
import com.storefront.service.StoreFrontUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Collection;
import java.util.Collections;

@Controller
@ConfigurationProperties(prefix = "server.handler")
public class RegisterController {

    @Autowired
    private StoreFrontUserService storeFrontUserService;

    @GetMapping("/register")
    public String homeRegister(final Model model) {
        if (!model.containsAttribute("registerUserForm")) {
            model.addAttribute("registerUserForm", new RegisterUserForm());
        }
        return "register";
    }

    @PostMapping("/register")
    public String createUser(final RegisterUserForm registerUserForm,
                             final RedirectAttributesModelMap redirectModel) {
        final UserData user = createUser(registerUserForm);

        final CreateUserDataResultData userResult = storeFrontUserService.createUser(user);

        String returnUrl;
        // TODO: add and replace apache utils
        if (CollectionUtils.isNotEmpty(userResult.getErrorMessages())) {
            redirectModel.addFlashAttribute("registerErrors", userResult.getErrorMessages());
            redirectModel.addFlashAttribute("registerUserForm", registerUserForm);
            returnUrl = "redirect:/register";
        } else {
            redirectModel.addFlashAttribute("newUserName", userResult.getUser().getName());
            redirectModel.addFlashAttribute("newUserSurName", userResult.getUser().getSurname());
            returnUrl = "redirect:/login";
        }

        return returnUrl;
    }

    private UserData createUser(RegisterUserForm registerUserForm) {
        // TODO: FRAMEWORK add an objectMapper, ex: ModelMapper, MapStruct, Dozer, Jmapper, Orika, etc. https://www.baeldung.com/java-performance-mapping-frameworks
        final UserData user = new UserData();
        user.setUserName(registerUserForm.getUsername());
        user.setName(registerUserForm.getName());
        user.setActive(Boolean.FALSE);
        user.setSurname(registerUserForm.getSurname());
        user.setContactData(createContact(registerUserForm));
        user.setRoleData(createRoles(registerUserForm));
        return user;
    }

    private Collection<RoleData> createRoles(final RegisterUserForm registerUserForm) {
        return Collections.emptyList();
    }

    private ContactData createContact(final RegisterUserForm registerUserForm) {
        final ContactData contactData = new ContactData();
        contactData.setEmail(registerUserForm.getEmail());
        contactData.setTelephone(registerUserForm.getTelephone());
        return contactData;
    }
}
