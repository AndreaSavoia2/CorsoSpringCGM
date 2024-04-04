package com.example.firstapp.controller;

import com.example.firstapp.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CarController {

    @Autowired List<Car> listaAuto;

    @GetMapping("/v1/cars")
    public List<Car> getCars(){
       return listaAuto;
    }

    @GetMapping("/v2/cars")
    public List<Car> getCars(@RequestParam String brand){
        List<Car> cars = new ArrayList<>();
        for (Car car : listaAuto){
            if(car.getBrand().equalsIgnoreCase(brand)){
                cars.add(car);
            }
        }
        return cars;
    }

    @GetMapping("/v2/cars/{brand}")
    public List<Car> getCars2(@PathVariable String brand){
        List<Car> cars = new ArrayList<>();
        for (Car car : listaAuto){
            if(car.getBrand().equalsIgnoreCase(brand)){
                cars.add(car);
            }
        }
        return cars;
    }

    @PostMapping("/v1/cars")
    public  List<Car> saveCars(@RequestBody Car c){
        listaAuto.add(c);
        return listaAuto;
    }

    @PatchMapping("/v1/cars")
    public  List<Car> patchCars(){
        List<Car> cars = new ArrayList<>();
        for (Car car : listaAuto){
            car.setTot(car.getTot() + 10);
            cars.add(car);
        }
        return cars;
    }

    @PutMapping("/v1/cars")
    public  List<Car> UpdateCars(){
        List<Car> cars = new ArrayList<>();
        for (Car car : listaAuto){
            if(car.getBrand().equals("Fiat")){
                car.setBrand(car.getBrand().toUpperCase());
                car.setModel(car.getModel().toUpperCase());
                car.setTot(car.getTot()-1);
                cars.add(car);
            }
        }
        return cars;
    }

    @DeleteMapping("/v1/cars")
    public  List<Car> DeleteCars(@RequestParam String model){
        List<Car> cars = new ArrayList<>();
        for (Car car : listaAuto){
            if(!car.getModel().equalsIgnoreCase(model)){
                cars.add(car);
            }
        }
        return cars;
    }

}
