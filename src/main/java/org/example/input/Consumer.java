package org.example.input;


import com.fasterxml.jackson.databind.json.JsonMapper;
import org.example.mapper.JacksonMapper;
import org.example.mapper.MapperWrapper;
import org.example.model.RegisterResult;
import org.example.service.AccountService;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Consumer {

    private final AccountService service;

    public Consumer(AccountService service) {
        this.service = service;
    }

    public void consume() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("operations.txt"), StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isBlank()) {
                continue;
            }
            JsonMapper mapper = JacksonMapper.get();
            MapperWrapper operation = mapper.readValue(line, MapperWrapper.class);
            if (operation.getAccount() != null) {
                RegisterResult response = service.initAccount(operation.getAccount());
                System.out.println(mapper.writeValueAsString(response));
            }
            if (operation.getTransaction() != null) {
                RegisterResult response = service.processTransaction(operation.getTransaction());
                System.out.println(mapper.writeValueAsString(response));
            }
        }
    }
}
