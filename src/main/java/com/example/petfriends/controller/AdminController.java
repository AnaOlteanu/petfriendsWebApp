package com.example.petfriends.controller;

import com.example.petfriends.model.CategoryEnum;
import com.example.petfriends.model.Event;
import com.example.petfriends.model.EventPlannerRequest;
import com.example.petfriends.model.Petshop;
import com.example.petfriends.service.AdminService;
import com.example.petfriends.service.CityService;
import com.example.petfriends.service.EventPlannerRequestService;
import com.example.petfriends.service.PetshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

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
    public String addPethopForm(Model model){
        model.addAttribute("petshop", new Petshop());
        model.addAttribute("cities", cityService.findAllCities());
        return "admin-petshop-add";
    }

    @PostMapping("/admin/addPetShop")
    public String submitEvent(@Valid @ModelAttribute("petshop") Petshop petshop,
                              BindingResult bindingResult,
                              Model model) throws IOException {


        if (bindingResult.hasErrors()){
            model.addAttribute("cities", cityService.findAllCities());
            return "admin-petshop-add";
        }
        Petshop savedPetshop = petshopService.save(petshop);
        return "redirect:/admin/petShops";
    }
}
