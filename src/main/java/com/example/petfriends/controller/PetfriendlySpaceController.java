package com.example.petfriends.controller;

import com.example.petfriends.model.PetfriendlySpace;
import com.example.petfriends.model.Post;
import com.example.petfriends.model.User;
import com.example.petfriends.service.PetfriendlySpaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class PetfriendlySpaceController {

    @Autowired
    private PetfriendlySpaceService petfriendlySpaceService;

    @GetMapping("/petfriendly/map")
    public ModelAndView getMap(){
        ModelAndView modelAndView = new ModelAndView("map");
        modelAndView.addObject("location", new PetfriendlySpace());
        return modelAndView;
    }

    @GetMapping("/petfriendly/spaces")
    public String getMap(Model model){
        List<PetfriendlySpace> petfriendlySpaces = petfriendlySpaceService.getAllPetfriendlySpaces();
        model.addAttribute("petfriendlySpaces", petfriendlySpaces);
        return "map-show";
    }

    @PostMapping(value = "/saveLocation")
    public String saveLocation( @Valid @ModelAttribute("location") PetfriendlySpace petfriendlySpace,
                               BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.info("binding results {} ", bindingResult.getAllErrors());
            return "map";
        }

        petfriendlySpaceService.save(petfriendlySpace);

        return "redirect:/petfriendly/spaces";
    }
}
