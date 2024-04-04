package com.example.firstapp.service;

import com.example.firstapp.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class CarService {

    @Bean("listaAuto")
    public List<Car> carList(){
        List<Car> cars = new ArrayList<>();
        Car c1 = new Car("Fiat", "500", 5);
        Car c2 = new Car("Toyota", "RAV4", 3);
        Car c3 = new Car("Peugeot", "5008", 6);
        Car c4 = new Car("Toyota", "Yaris", 7);


        cars.add(c1); log.info(c1.toString());
        cars.add(c2); log.info(c2.toString());
        cars.add(c3); log.info(c3.toString());
        cars.add(c4); log.info(c4.toString());

        return cars;
    }

}
