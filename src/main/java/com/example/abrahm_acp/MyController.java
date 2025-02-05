package com.example.abrahm_acp;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class MyController {

    public final String UUID = "s2772245";
    public Map<String, String> myMap = new HashMap<>();

    @GetMapping("/uuid")
    public String getUuid() {
        return "<html><body><h1>" + UUID + "</h1></body></html>";
    }

    @PostMapping("/valuemanager")
    public void keyValueWithParams(@RequestParam String key, @RequestParam String value, HttpServletResponse res){
        myMap.put(key, value);
        res.setStatus(HttpServletResponse.SC_OK);
        //return "Success!";
    }

    @PostMapping("/valuemanager/{key}/{value}")
    public void keyValueWithPath(@PathVariable String key, @PathVariable String value, HttpServletResponse res){
        myMap.put(key, value);
        res.setStatus(HttpServletResponse.SC_OK);
        //return "Success!";
    }

    @DeleteMapping("/valuemanager/{key}")
    public void deleteKeyValue(@PathVariable String key){
        myMap.remove(key);
    }

    @GetMapping(value = "/valuemanager/{key}", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getValue(@PathVariable(required = false) String key) {
        if (key == null || key.isEmpty()){
            return ResponseEntity.ok(myMap);
        }

        if (myMap.containsKey(key)){
            return ResponseEntity.ok(myMap.get(key));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/valuemanager", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> getValue() {

        return ResponseEntity.ok(myMap);
    }

}
