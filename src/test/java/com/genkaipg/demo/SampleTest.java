package com.genkaipg.demo;

import com.genkaipg.demo.constants.SecurityConfigConstans;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SampleTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="genkaipg",roles={"ADMIN"})
    public void adminTest() {
        try {
            mockMvc.perform(get("/admin/test"))
                    .andExpect(status().isOk()).andDo(print())
                    .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.helloSay").value("Hello genkaipg. You are admin!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username="guest",roles={"USER"})
    public void userTest() {
        try {
            String message = "Bearer error=\"insufficient_scope\"";
            mockMvc.perform(get("/admin/test"))
                    .andExpect(status().isUnauthorized()).andDo(print())
                    .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(header().string(SecurityConfigConstans.AUTHENTICATE_HEADER,message))
                    .andExpect(jsonPath("$.errors[0].message").value(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sessionErrorTest() {
        try {
            String message = "Bearer realm=\"Realm\"";
            mockMvc.perform(get("/admin/test").header(SecurityConfigConstans.XHR_FIELDS_IS_AJAX_KEY, SecurityConfigConstans.XHR_FIELDS_IS_AJAX_VALUE))
                    .andExpect(status().isUnauthorized()).andDo(print())
                    .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_VALUE))
                    .andExpect(header().string(SecurityConfigConstans.AUTHENTICATE_HEADER,message))
                    .andExpect(jsonPath("$.errors[0].message").value(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}