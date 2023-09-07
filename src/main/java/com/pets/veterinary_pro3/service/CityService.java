package com.pets.veterinary_pro3.service;

import com.pets.veterinary_pro3.exceptions.VeterinaryException;
import com.pets.veterinary_pro3.model.City;
import com.pets.veterinary_pro3.model.Vaccine;
import com.pets.veterinary_pro3.model.Vet;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Data
public class CityService {
    private List<Vet> vets;
    private List<City> cities;
    private List<Vaccine> vaccine;

    public CityService() {
        cities = new ArrayList<>();
        cities.add(new City("16917001", "Manizales"));
        cities.add(new City("16966001", "Pereira"));
        cities.add(new City("16905001", "Medellin"));
        cities.add(new City("16917042", "Anserma"));
        cities.add(new City("16941001", "Neiva"));
        cities.add(new City("16941807", "Timana"));
        cities.add(new City("16941791", "Tarqui"));
        cities.add(new City("16941551", "Pitalito"));
        cities.add(new City("16981001", "Arauca"));
        cities.add(new City("16905467", "MonteBello"));

    }

    public City findCityById(String id) throws VeterinaryException {
        for (City cityFound : this.getCities()) {
            if (cityFound.getCode().equals(id)) {
                return cityFound;
            }
        }
        //no lo encontro
        throw new VeterinaryException("la ciudad con" + id + "no existe");
    }

    public City findCityByTitle(String description) throws VeterinaryException {
        for (City cities : this.getCities()) {
            if (cities.getDescription().equalsIgnoreCase(description)) {
                return cities;
            }
        }
        throw new VeterinaryException("La ciudad con " + description + "no existe");
    }

    public List<City> findCitiesByInitialLetter(char initialLetter) {
        List<City> citiesFound = new ArrayList<>();
        for (City city : this.getCities()) {
            if (city.getDescription().charAt(0) == initialLetter) {
                citiesFound.add(city);
            }
        }
        return citiesFound;
    }

    public String addCity(City city) throws VeterinaryException{
        //verificar si existe
        if(this.verifyCityExist(city)){
            throw new VeterinaryException("El codigo ingresado ya existe");
        }
        else{
            this.cities.add(city);

        }
        return "Ciudad adicionada correctamente";
    }

    private boolean verifyCityExist(City city){
        for(City cityAct: this.cities){
            if(city.getCode().equals(cityAct.getCode())){
                return true;
            }
        }
        return false;
    }

    public String updateCity(String code, City city) throws VeterinaryException{
        for(City cityAct : this.cities){
            if(cityAct.getCode().equals(code)){
                cityAct.setDescription(city.getDescription());
                return "Ciudad actualizada correctamente";
            }
        }
        throw new VeterinaryException("El código ingresado no existe");

    }//End UpdateCity
}
