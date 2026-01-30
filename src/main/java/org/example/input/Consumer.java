package org.example.input;


import org.example.mapper.JacksonMapper;
import org.example.mapper.MapperWrapper;
import org.example.models.ResponseDTO;
import org.example.service.AccountService;
import tools.jackson.databind.json.JsonMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Consumer {

    private final AccountService service;

    public Consumer(AccountService service) {
        this.service = service;
    }

    public void consume() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isBlank()) {
                continue;
            }
            JsonMapper mapper = JacksonMapper.get();
            MapperWrapper operation = mapper.readValue(line, MapperWrapper.class);
            if(operation.getAccount() != null) {
                ResponseDTO response = service.initAccount(operation.getAccount());
                System.out.println(response);
            }
            if(operation.getTransaction() != null) {
                ResponseDTO response = service.processTransaction(operation.getTransaction());
                System.out.println(response);
            }
        }
    }
}
