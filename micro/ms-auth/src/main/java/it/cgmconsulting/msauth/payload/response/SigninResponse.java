package it.cgmconsulting.msauth.payload.response;

import it.cgmconsulting.msauth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SigninResponse {

    private String username;
    private String role;
    private String token;
}
