package it.cgmconsulting.gateway.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class JwtUser {

    private String id;
    private String role;
}
