package com.example.petfriends.controller;

import com.example.petfriends.model.City;
import com.example.petfriends.model.Petshop;
import com.example.petfriends.service.CityService;
import com.example.petfriends.service.PetshopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class PetshopController {

    @Autowired
    private PetshopService petshopService;

    @Autowired
    private CityService cityService;

    @GetMapping("/petshop/list")
    public ModelAndView listPetshops(){

        List<Petshop> petshops = petshopService.findAll();

        ModelAndView modelAndView = new ModelAndView("petshop-list");
        modelAndView.addObject("petshops", petshops);
        modelAndView.addObject("cities", cityService.findAllCities());
        return modelAndView;
    }

    @RequestMapping("/petshop/search")
    public String searchPetshopByCity(@RequestParam("city") String cityName,
                             Model model) {

        log.info("city {}", cityName);
        Optional<City> cityOptional = cityService.findByName(cityName);
        if(cityOptional.isPresent()) {
            List<Petshop> petshops = petshopService.findByCity(cityOptional.get());
            log.info("petshops {} ", petshops.size());
            model.addAttribute("petshops", petshops);
            model.addAttribute("cities", cityService.findAllCities());
            model.addAttribute("city", cityName);
        }
        else {
            model.addAttribute("petshops", null);
            model.addAttribute("cities", cityService.findAllCities());
            model.addAttribute("city", cityName);
        }

        return "petshop-list";
    }
}
