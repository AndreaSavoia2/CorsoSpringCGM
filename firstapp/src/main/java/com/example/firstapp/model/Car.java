package com.example.firstapp.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode //imposta equal e has a tutti
//@EqualsAndHashCode(onlyExplicitlyIncluded = true) imposta equal e has solo a quelli inclusi
//@Data ottiene tutto escluso i costruttori
public class Car {

    private String brand;
    private String model;
    private int tot;

}
