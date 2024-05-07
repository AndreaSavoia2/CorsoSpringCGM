package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(

        @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$",
                message = "La password deve contenere solo caratteri maiuscoli e/0 minuscoli e numeri tra 6 e 10 caratteri")
        String newPwd,
        @NotBlank
        String newPwdConfirm,
        @NotBlank
        String oldPwd

) {}
