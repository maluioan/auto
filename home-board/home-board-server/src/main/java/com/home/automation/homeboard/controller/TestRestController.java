package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.BoardData;
import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.service.HomeBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestRestController {


}


//
//    @Test
//    public void giventUrl_whenGetRequest_thenFindGetResponse()
//            throws Exception {
//
//        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
//                .get("/get");
//
//        ResultMatcher contentMatcher = MockMvcResultMatchers.content()
//                .string("GET Response");
//
//        this.mockMvc.perform(builder).andExpect(contentMatcher)
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//    }