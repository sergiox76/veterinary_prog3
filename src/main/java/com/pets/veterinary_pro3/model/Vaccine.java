package com.pets.veterinary_pro3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class Vaccine {
    private City city;
    private Vet vet;
    private Short quantity;
}
