package com.example.abrahm_acp;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class MyController {

    private final RestTemplate restTemplate;
    public final String UUID = "s2772245";
    public Map<String, String> myMap = new HashMap<>();

    public MyController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    private record ServiceRequest(String externalBaseUrl, String parameters) {}

    @GetMapping("/uuid")
    public String getUUID(){
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

    @PostMapping("/callservice")
    public ResponseEntity<?> callExtService(@RequestBody ServiceRequest request) {
        try {
            // Trim parameters and construct path
            String path = request.parameters().trim();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            URI uri = UriComponentsBuilder
                    .fromUriString(request.externalBaseUrl())
                    .path(path)
                    .build()
                    .encode()
                    .toUri();

            RequestEntity<Void> req = RequestEntity.get(uri)
                    .accept(MediaType.ALL)
                    .build();

            ResponseEntity<byte[]> response = restTemplate.exchange(req, byte[].class);

            MediaType contentType = response.getHeaders().getContentType();
            if (contentType == null) {
                contentType = MediaType.TEXT_PLAIN;
            }

            return ResponseEntity.status(response.getStatusCode())
                    .contentType(contentType)
                    .body(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(null);
        }
    }



}
