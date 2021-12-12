package com.example.shareformore.response;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseHolder {
    private final int status;
    private final String data;
    private final String msg;
    private final List<?> list;
    private final Object obj;
    private final String token;

    public ResponseHolder(int status, String data, String msg, List<?> list, Object obj, String token) {
        this.status = status;
        this.data = data;
        this.msg = msg;
        this.list = list;
        this.obj = obj;
        this.token = token;
    }

    public ResponseEntity<?> getResponseEntity() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("data", data);
        map.put("msg", msg);
        map.put("list", list);
        map.put("obj", obj);
        map.put("token", token);
        return ResponseEntity.status(status).body(map);
    }
}
