package com.pets.veterinary_pro3.controller;

import com.pets.veterinary_pro3.controller.dto.ResponseDTO;
import com.pets.veterinary_pro3.exceptions.VeterinaryException;
import com.pets.veterinary_pro3.model.City;
import com.pets.veterinary_pro3.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping(path = "/City")
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping(path = "/cities")
    public ResponseEntity<ResponseDTO> getAllCities() {
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(),
                        cityService.getCities(),
                        null),
                HttpStatus.OK);
    }

    @GetMapping(path = "/cities/{id}")
    public ResponseEntity<ResponseDTO> getCityByID(@PathVariable String id) {
        try {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.OK.value(),
                            cityService.findCityById(id),
                            null),
                    HttpStatus.OK);
        } catch (VeterinaryException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(),
                            null,
                            errors),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/cities/description/{description}")
    public ResponseEntity<ResponseDTO> getCityByTitle(@PathVariable String description) {
        try {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.OK.value(),
                            cityService.findCityByTitle(description),
                            null),
                    HttpStatus.OK);
        } catch (VeterinaryException er) {
            List<String> errors = new ArrayList<>();
            errors.add(er.getMessage());
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(),
                            null, errors),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/cities/by_initial_letter/{letter}")
    public ResponseEntity<ResponseDTO> getCitiesByInitialLetter(
            @PathVariable char letter
    ) {
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(),
                        cityService.findCitiesByInitialLetter(letter),
                        null)
                , HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ResponseDTO> createCity(
            @RequestBody City city){
        try {

            return new ResponseEntity<>(
                    new ResponseDTO(
                            HttpStatus.OK.value(),
                            cityService.addCity(city),
                            null
                    ), HttpStatus.OK
            );
        } catch (VeterinaryException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.CONFLICT.value(),
                            null,
                            errors),
                    HttpStatus.OK);
        }
    }//End CreateCity

    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponseDTO> updateCity(
            @PathVariable String id, @RequestBody City city){
        try {

            return new ResponseEntity<>(
                    new ResponseDTO(
                            HttpStatus.OK.value(),
                            cityService.updateCity(id,city),
                            null
                    ), HttpStatus.OK
            );
        } catch (VeterinaryException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(),
                            null,
                            errors),
                    HttpStatus.OK);
        }
    }//End updateCity

}
