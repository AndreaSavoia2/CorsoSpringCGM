package it.cgmconsulting.mspost.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PostRequest {

    @NotBlank @Size(max = 100, min = 1)
    private String title;

    @Size(max = 255)
    private String postImage;

    private int author;

}
