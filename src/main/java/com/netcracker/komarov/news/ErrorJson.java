package com.netcracker.komarov.news;

import org.springframework.stereotype.Component;

@Component
public class ErrorJson {
    public String getErrorJson(String error) {
        return "{\"error\":\"" + error + "\"}";
    }
}
