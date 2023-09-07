package com.pets.veterinary_pro3.controller.dto;

import com.pets.veterinary_pro3.model.City;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class VetDTO {
    private String code;
    private String name;
    private Byte age;
    private String code_city;
    private City city;
}
