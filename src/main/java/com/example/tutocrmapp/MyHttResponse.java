package com.example.tutocrmapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class MyHttResponse {

    public static ResponseEntity<Object> response(HttpStatus status, String message, Object data)
    {
        HashMap<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status.value());

        if (data != null)
        {
            if (status == HttpStatus.BAD_REQUEST)
                map.put("errors", data);
            else
                map.put("data", data);
        }

        return new ResponseEntity<>(map, status);
    }
}
