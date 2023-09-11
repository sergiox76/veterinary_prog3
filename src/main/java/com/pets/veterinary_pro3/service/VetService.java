package com.pets.veterinary_pro3.service;

import com.pets.veterinary_pro3.controller.dto.CountVetByCityDTO;
import com.pets.veterinary_pro3.exceptions.VeterinaryException;
import com.pets.veterinary_pro3.model.City;
import com.pets.veterinary_pro3.model.Vaccine;
import com.pets.veterinary_pro3.model.Vet;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
public class VetService {
    private List<Vet> vets;
    private List<City> cities;
    private List<Vaccine> vaccine;



    public VetService(CityService cityService) {
        vets = new ArrayList<>();
        vets.add(new Vet("1012345", "Jhon", (byte) 18,new City("16941551","Pitalito")));
        vets.add(new Vet("1013345", "Sergio", (byte) 19,new City("16941551","Pitalito")));
        vets.add(new Vet("1014235", "Sebastian", (byte) 19,new City("16917001","Manizales")));
        vets.add(new Vet("1015345", "Valeria", (byte) 20,new City("16905001","Medellin")));
        vets.add(new Vet("1016345", "Carlos", (byte) 27,new City("16981001","Arauca")));
        vets.add(new Vet("1017345", "Veronica", (byte) 22,new City("16981001","Arauca")));
        vets.add(new Vet("1018345","Jhair",(byte)19,new City("16941791","Tarqui")));
    }

    public Vet findVetById(String code) throws VeterinaryException {
        for (Vet vetFound : this.getVets()) {
            if (vetFound.getCode().equals(code)) {
                return vetFound;
            }
        }
        throw new VeterinaryException("El veterinario con " + code + "no existe");
    }

    public Vet findVetByName(String name) throws VeterinaryException {
        for (Vet vets : this.getVets()) {
            if (vets.getName().equalsIgnoreCase(name)) {
                return vets;
            }
        }
        throw new VeterinaryException("El veterinario con " + name + "no existe");
    }

    public List<Vet> findVetByLetter(char inicial) throws VeterinaryException {
        List<Vet> vetsEncontrados = new ArrayList<>();
        for (Vet vets : this.getVets()) {
            if (vets.getName().charAt(0) == Character.toUpperCase(inicial) || vets.getName().charAt(0) == Character.toLowerCase(inicial)) {
                vetsEncontrados.add(vets);
            }
        }
        if (vetsEncontrados.isEmpty()) {
            throw new VeterinaryException("El veterinario con " + inicial + "no existe");
        } else {
            return vetsEncontrados;
        }
    }

    public List<Vet> findVetByMinAge() {
        List<Vet> vetMinAge = new ArrayList<>();
        int minAge = Integer.MAX_VALUE;

        for (Vet vet : vets) {
            int age = Integer.parseInt(String.valueOf(vet.getAge()));
            if (age < minAge) {
                minAge = age;
                vetMinAge.clear();
                vetMinAge.add(vet);
            } else if (age == minAge) {
                vetMinAge.add(vet);
            }
        }
        return vetMinAge;

    }
    public List<Vet> findVetBetAge(int minAge, int maxAge) {
        List<Vet> vetBetAges = new ArrayList<>();

        for (Vet vet : vets) {
            int age = Integer.parseInt(String.valueOf(vet.getAge()));
            if (age >= minAge && age <= maxAge) {
                vetBetAges.add(vet);
            }
        }

        return vetBetAges;
    }
    public String addVet(Vet vet) throws VeterinaryException{
        if(this.verifyVetExist(vet)){
            throw new VeterinaryException("El código ingresado ya existe");
        }
        else{
                this.vets.add(vet);
            }

        return "veterinario adicionado correctamente";
    }

    private boolean verifyVetExist(Vet vets){
        for(Vet vetAct: this.vets){
            if(vets.getCode().equals(vetAct.getCode())){
                return true;
            }
        }
        return false;
    }

    public String updateVet(String code, Vet vets) throws VeterinaryException{
        for(Vet vetAct : this.vets){
            if(vetAct.getCode().equals(code)){
                vetAct.setName(vets.getName());
                return "veterinario actualizado correctamente";
            }
        }
        throw new VeterinaryException("El código ingresado no existe");

    }
    public List<CountVetByCityDTO> countVetsByCity() {
        //Map<String, Integer> vetCountByCity = new HashMap<>();
        List<CountVetByCityDTO> vetsCountByCity = new ArrayList<>();

        for (Vet vet : vets) {
            City city = vet.getCity();
            if (city != null) {
                String cityDescription = city.getDescription();
                //vetCountByCity.put(cityDescription, vetCountByCity.getOrDefault(cityDescription
                       // , 0) + 1);
                Optional<CountVetByCityDTO> countVetByCityDTOOptional = vetsCountByCity.stream().filter(
                   x -> x.getName().equals(cityDescription)
                ).findAny();
                if (countVetByCityDTOOptional.isPresent()){
                    countVetByCityDTOOptional.get().setQuantity(countVetByCityDTOOptional.get().getQuantity()+1);
                }
                else {
                    vetsCountByCity.add(new CountVetByCityDTO(cityDescription,1));
                }
            }
        }

        return vetsCountByCity;
    }
    public List<Vet> getVetsBySyllableAndSort(String syllable, String sortBy) {
        if (syllable.length() != 2) {
            throw new IllegalArgumentException("La sílaba debe tener exactamente dos letras.");
        }
        CharSequence miCharSequence = syllable.toLowerCase().subSequence(0, syllable.length());

        List<Vet> filteredVets = vets.stream()
                //.filter(vet -> containsSyllable(vet.getName().toLowerCase(), syllable.toLowerCase()))
                .filter(vet -> vet.getName().toLowerCase().contains(miCharSequence))
                .collect(Collectors.toList());
        if ("age".equalsIgnoreCase(sortBy)) {
            filteredVets.sort(Comparator.comparingInt(Vet::getAge));
        } else {
            filteredVets.sort(Comparator.comparing(Vet::getName));
        }

        return filteredVets;
    }

    private boolean containsSyllable(String name, String syllable) {
        // Ensure that the syllable has exactly two letters
        if (syllable.length() != 2) {
            throw new IllegalArgumentException("La sílaba debe tener exactamente dos letras.");
        }

        int syllableLength = 2; // Two-letter syllable
        for (int i = 0; i <= name.length() - syllableLength; i++) {
            String substring = name.substring(i, i + syllableLength);
            if (substring.equals(syllable)) {
                return true;
            }
        }
        return false;

   }
}
