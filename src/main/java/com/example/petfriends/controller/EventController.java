package com.example.petfriends.controller;

import com.example.petfriends.model.*;
import com.example.petfriends.service.EventPlannerRequestService;
import com.example.petfriends.service.EventService;
import com.example.petfriends.service.RoleService;
import com.example.petfriends.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.example.petfriends.model.RoleEnum.ROLE_EVENT_PLANNER;

@Controller
@Slf4j
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EventPlannerRequestService eventPlannerRequestService;

    @GetMapping("/event/list")
    public ModelAndView listEvents() {

        User authenticatedUser = userService.getAuthenticatedUser();
        List<Event> currentEvents = eventService.getCurrentEvents();
        List<Event> endedEvents = eventService.getEndedEvents();
        List<Event> enteredEvents = authenticatedUser.getEventsEntered();
        List<Event> plannedEvents = authenticatedUser.getEventsPlanned();
        Long numberPosts = (long) authenticatedUser.getPosts().size();
        Role plannerRole = roleService.findByRoleName(ROLE_EVENT_PLANNER);
        boolean isPlanner = authenticatedUser.getRoles().contains(plannerRole);
        EventPlannerRequest request = eventPlannerRequestService.findRequestByUsername(authenticatedUser.getUsername());
        String requestStatus = "";

        if(request != null) {
           requestStatus = request.getStatus().toString();
        }

        ModelAndView modelAndView = new ModelAndView("event-list");
        modelAndView.addObject("user", authenticatedUser);
        modelAndView.addObject("currentEvents", currentEvents);
        modelAndView.addObject("endedEvents", endedEvents);
        modelAndView.addObject("enteredEvents", enteredEvents);
        modelAndView.addObject("plannedEvents", plannedEvents);
        modelAndView.addObject("numberPosts", numberPosts);
        modelAndView.addObject("isPlanner", isPlanner);
        modelAndView.addObject("requestStatus", requestStatus);

        log.info("Current events {} ", currentEvents);
        log.info("Ended Events {} ", endedEvents);
        log.info("Is user planner {} ", isPlanner);
        log.info("Request status {} ", requestStatus);

        return modelAndView;

    }


}
