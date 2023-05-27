package com.example.petfriends.controller;

import com.example.petfriends.model.EventPlannerRequest;
import com.example.petfriends.service.AdminService;
import com.example.petfriends.service.EventPlannerRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private EventPlannerRequestService eventPlannerRequestService;

    @GetMapping("/admin/requests")
    public ModelAndView getAllRequests(){

        List<EventPlannerRequest> requests = eventPlannerRequestService.findAll();
        ModelAndView modelAndView = new ModelAndView("admin-requests");
        modelAndView.addObject("requests", requests);

        return modelAndView;
    }

}
