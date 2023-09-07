package com.pets.veterinary_pro3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Vet {
    private String code;
    private String name;
    private Byte age;
    private City city;
}
