package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SigninRequest {

    @NotBlank @Size(max = 20, min = 4)
    private String username;

    @Pattern(
            regexp = "^[a-zA-Z0-9]{6,10}$",
            message = "La password deve contenere solo caratteri maiuscoli e/0 minuscoli e numeri tra 6 e 10 caratteri")
    private String password;

}
