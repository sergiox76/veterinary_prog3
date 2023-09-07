package com.pets.veterinary_pro3.controller;

import com.pets.veterinary_pro3.controller.dto.ResponseDTO;
import com.pets.veterinary_pro3.controller.dto.VetDTO;
import com.pets.veterinary_pro3.exceptions.VeterinaryException;
import com.pets.veterinary_pro3.model.City;
import com.pets.veterinary_pro3.model.Vet;
import com.pets.veterinary_pro3.service.CityService;
import com.pets.veterinary_pro3.service.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/vet")
public class VetController {
    @Autowired
    private VetService veterinaryService;
    @Autowired
    private CityService cityService;

    @GetMapping(path = "/vets")
    public ResponseEntity<ResponseDTO> getAllVets() {
        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(),
                        veterinaryService.getVets(),
                        null),
                HttpStatus.OK);
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<ResponseDTO> getVetById(@PathVariable String id) {
        try {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.OK.value(),
                            veterinaryService.findVetById(id),
                            null),
                    HttpStatus.OK);
        } catch (VeterinaryException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(),
                            null, errors),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/description/{description}")
    public ResponseEntity<ResponseDTO> getVetByName(@PathVariable String description) {
        try {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.OK.value(),
                            veterinaryService.findVetByName(description),
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

    @GetMapping(path = "/letter/{name}")
    public ResponseEntity<ResponseDTO> getVetByLetter(@PathVariable char name) {
        try {
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.OK.value(),
                            veterinaryService.findVetByLetter(name),
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

    @GetMapping(path = "/minimum_age")
    public ResponseEntity<ResponseDTO> getVetWithMinimumAge() {
        List<Vet> vetsWithMinimumAge = veterinaryService.findVetByMinAge();

        if (vetsWithMinimumAge.isEmpty()) {
            List<String> errors = new ArrayList<>();
            errors.add("No veterinarians found.");
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(),
                            null, errors),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(), vetsWithMinimumAge,
                        null),
                HttpStatus.OK);
    }

    @GetMapping(path = "/range_age/{min}/{max}")
    public ResponseEntity<ResponseDTO> getVetsInAgeRange(
            @PathVariable int min,
            @PathVariable int max
    ) {
        if (min > max) {
            List<String> errors = new ArrayList<>();
            errors.add("Invalid age range.");
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.BAD_REQUEST.value(), null, errors),
                    HttpStatus.OK);
        }

        List<Vet> vetsInAgeRange = veterinaryService.findVetBetAge(min, max);

        if (vetsInAgeRange.isEmpty()) {
            List<String> errors = new ArrayList<>();
            errors.add("No veterinarians found in the specified age range.");
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, errors),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                new ResponseDTO(HttpStatus.OK.value(), vetsInAgeRange, null),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createVets(
            @RequestBody VetDTO vetDTO) {
        try {
            // Busca la ciudad correspondiente por su código
            City city = cityService.findCityById(vetDTO.getCode_city()); // Asume que tienes un método findCityByCode en CityService

            if (city == null) {
                List<String> errors = new ArrayList<>();
                errors.add("La ciudad con código " + vetDTO.getCode_city() + " no existe.");
                return new ResponseEntity<>(
                        new ResponseDTO(HttpStatus.BAD_REQUEST.value(), null, errors),
                        HttpStatus.BAD_REQUEST);
            }

            // Crea un objeto Vet a partir de VetDTO
            Vet vet = new Vet();
            vet.setCode(vetDTO.getCode());
            vet.setName(vetDTO.getName());
            vet.setAge(vetDTO.getAge());
            vet.setCity(city);

            // Llama al servicio para agregar el veterinario
            String result = veterinaryService.addVet(vet);

            return new ResponseEntity<>(
                    new ResponseDTO(
                            HttpStatus.OK.value(),
                            result,
                            null
                    ),
                    HttpStatus.OK
            );
        } catch (VeterinaryException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return new ResponseEntity<>(
                    new ResponseDTO(HttpStatus.CONFLICT.value(),
                            null,
                            errors),
                    HttpStatus.CONFLICT
            );
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponseDTO> updateVets(
            @PathVariable String id, @RequestBody Vet vets) {
        try {

            return new ResponseEntity<>(
                    new ResponseDTO(
                            HttpStatus.OK.value(),
                            veterinaryService.updateVet(id, vets),
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
    }

    @GetMapping("/countByCity")
    public ResponseEntity<Map<String, Integer>> countVetsByCity() {
        Map<String, Integer> vetCountByCity = veterinaryService.countVetsByCity();
        return ResponseEntity.ok(vetCountByCity);
    }

    @GetMapping("/vets/{syllable}/{sortby}")
    public ResponseEntity<ResponseDTO> getVetsBySyllableAndSort(
            @PathVariable String syllable,
            @PathVariable(required = false) String sortby
    ) {
        if (sortby == null) {
            sortby = "name"; // Default sorting by name if not provided
        }

        try {
            List<Vet> vets = veterinaryService.getVetsBySyllableAndSort(syllable, sortby);

            if (vets.isEmpty()) {
                List<String> errors = new ArrayList<>();
                errors.add("No se encontraron veterinarios con la sílaba especificada");
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, errors));
            }

            return ResponseEntity
                    .ok(new ResponseDTO(HttpStatus.OK.value(), vets, null));
        } catch (IllegalArgumentException e) {
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), null, errors));

        }

    }
}