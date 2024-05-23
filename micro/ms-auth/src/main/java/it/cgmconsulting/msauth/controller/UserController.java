package it.cgmconsulting.msauth.controller;

import it.cgmconsulting.msauth.payload.request.SignupRequest;
import it.cgmconsulting.msauth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request){
        return service.signup(request);
    }

}
