package strom.code.keycloak.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import strom.code.keycloak.domain.Request;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/test")
public class AuthController {

    RestTemplate restTemplate = new RestTemplate();

    private static final String resourceUrl = "http://localhost:8080/realms/Demo-Realm/protocol/openid-connect/token";

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<Object> getAnonymous(@RequestBody Request request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "springboot-keycloak");
        map.add("client_secret", "mgq9zPxAhbaBvzrzwr74XJN7KaEmT2nV");
        map.add("username", request.getUsername());
        map.add("password", request.getPassword());

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(map, headers);

        ResponseEntity<Object> res = restTemplate.postForEntity(
                resourceUrl, req , Object.class);

        System.err.println(res);

        return ResponseEntity.ok(res.getBody());
    }


    @RolesAllowed("app-user")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<String> getUser(@RequestHeader String Authorization) {
        return ResponseEntity.ok("Hello User");
    }

    @RolesAllowed("app-admin")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<String> getAdmin(@RequestHeader String Authorization) {
        return ResponseEntity.ok("Hello Admin");
    }

    @RolesAllowed({ "app-admin", "app-user" })
    @RequestMapping(value = "/all-user", method = RequestMethod.GET)
    public ResponseEntity<String> getAllUser(@RequestHeader String Authorization) {
        return ResponseEntity.ok("Hello All User");
    }

}