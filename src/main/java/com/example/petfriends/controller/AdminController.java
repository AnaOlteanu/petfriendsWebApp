package com.example.petfriends.controller;

import com.example.petfriends.model.*;
import com.example.petfriends.service.AdminService;
import com.example.petfriends.service.CityService;
import com.example.petfriends.service.EventPlannerRequestService;
import com.example.petfriends.service.PetshopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
public class AdminController {


    @Autowired
    private EventPlannerRequestService eventPlannerRequestService;

    @Autowired
    private PetshopService petshopService;

    @Autowired
    private CityService cityService;


    @GetMapping("/admin/requests")
    public ModelAndView getAllRequests(){

        List<EventPlannerRequest> requests = eventPlannerRequestService.findAll();
        ModelAndView modelAndView = new ModelAndView("admin-requests");
        modelAndView.addObject("requests", requests);

        return modelAndView;
    }

    @GetMapping("/admin/petShops")
    public ModelAndView listPethops(){
        List<Petshop> petshops = petshopService.findAll();
        ModelAndView modelAndView = new ModelAndView("admin-petshops");
        modelAndView.addObject("petshops", petshops);
        return modelAndView;
    }

    @GetMapping("/admin/addPetShop")
    public String addPetshopForm(Model model){
        model.addAttribute("petshop", new Petshop());
        model.addAttribute("cities", cityService.findAllCities());
        return "admin-petshop-add";
    }

    @PostMapping("/admin/addPetShop")
    public String addPetshop(@Valid @ModelAttribute("petshop") Petshop petshop,
                              BindingResult bindingResult,
                              Model model) {


        if (bindingResult.hasErrors()){
            model.addAttribute("cities", cityService.findAllCities());
            return "admin-petshop-add";
        }
        Petshop savedPetshop = petshopService.save(petshop);
        return "redirect:/admin/petShops";
    }

    @RequestMapping("/admin/deletePetshop/{idPetshop}")
    public String deletePetshopById(@PathVariable("idPetshop") Long idPetshop){

        petshopService.deleteById(idPetshop);

        return "redirect:/admin/petShops";
    }

    @GetMapping("/admin/editPetshop/{idPetshop}")
    public ModelAndView editPetshopById(@PathVariable("idPetshop") Long idPetshop){

        Petshop petshop = petshopService.findById(idPetshop);
        ModelAndView modelAndView = new ModelAndView("admin-petshop-edit");
        modelAndView.addObject("petshop", petshop);
        modelAndView.addObject("cities", cityService.findAllCities());

        return modelAndView;
    }

    @PostMapping("/admin/editPetshop")
    public String editPetshop(@ModelAttribute @Valid Petshop petshop,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin-petshop-edit";
        }
        Petshop savedPetshop = petshopService.save(petshop);
        log.info("Successfully edited petshop with id {}", savedPetshop.getIdPetShop());
        return "redirect:/admin/petShops" ;
    }
}
