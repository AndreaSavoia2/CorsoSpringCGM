package it.cgmconsulting.msauth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SimpleUserResponse {

    private int id;
    private String username;
}
