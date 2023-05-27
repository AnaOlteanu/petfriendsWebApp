package com.example.petfriends.controller;

import com.example.petfriends.model.EventPlannerRequest;
import com.example.petfriends.model.RequestStatus;
import com.example.petfriends.model.Role;
import com.example.petfriends.model.User;
import com.example.petfriends.service.EventPlannerRequestService;
import com.example.petfriends.service.RoleService;
import com.example.petfriends.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.example.petfriends.model.RoleEnum.ROLE_EVENT_PLANNER;

@Controller
public class EventPlannerRequestController {

    @Autowired
    private EventPlannerRequestService eventPlannerRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/becomePlanner/{idUser}")
    public String becomePlanner(@PathVariable("idUser") Long idUser) {
        User user = userService.findById(idUser);
        EventPlannerRequest request = eventPlannerRequestService.createRequest(user);
        return "redirect:/requestSent";
    }

    @GetMapping("/requestSent")
    public String requestSent(){
        return "request-sent";
    }

    @RequestMapping("/request/{idRequest}/approve")
    public String approveRequest(@PathVariable("idRequest") Long idRequest) {

        EventPlannerRequest request = eventPlannerRequestService.findById(idRequest);

        request.setStatus(RequestStatus.APPROVED);
        EventPlannerRequest savedRequest = eventPlannerRequestService.save(request);

        User user = request.getUser();
        user.getRoles().add(roleService.findByRoleName(ROLE_EVENT_PLANNER));
        userService.saveWithoutHash(user);

        return "redirect:/admin/requests";

    }
}
