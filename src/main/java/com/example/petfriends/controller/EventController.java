package com.example.petfriends.controller;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.*;
import com.example.petfriends.model.recommendation.EventRecommender;
import com.example.petfriends.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private CityService cityService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private EventRecommender eventRecommender;

    @GetMapping("/event/list")
    public ModelAndView listEvents() {

        User authenticatedUser = userService.getAuthenticatedUser();
        List<Event> currentEvents = eventService.getCurrentEvents();
        List<Event> endedEvents = eventService.getEndedEvents();
        List<Event> futureEvents = eventService.getFutureEvents();
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
        modelAndView.addObject("futureEvents", futureEvents);
        modelAndView.addObject("numberPosts", numberPosts);
        modelAndView.addObject("isPlanner", isPlanner);
        modelAndView.addObject("requestStatus", requestStatus);

        log.info("Current events {} ", currentEvents);
        log.info("Ended Events {} ", endedEvents);
        log.info("Is user planner {} ", isPlanner);
        log.info("Request status {} ", requestStatus);

        return modelAndView;

    }

    @GetMapping("/event/add")
    public String newEvent(Model model) {
        model.addAttribute("event", new Event());
        List<CategoryEnum> categoriesAll = List.of(CategoryEnum.values());
        model.addAttribute("categoriesAll", categoriesAll );
        model.addAttribute("cities", cityService.findAllCities());
        return "event-add";
    }

    @PostMapping("/event")
    public String submitEvent(@Valid @ModelAttribute("event") Event event,
                              BindingResult bindingResult,
                              Model model,
                              @RequestParam("eventimages")MultipartFile[] images) throws IOException {
        log.info("image length {}", images.length);

        if (bindingResult.hasErrors()){
            log.info("Binding errors event are {} ", bindingResult.getAllErrors());
            List<CategoryEnum> categoriesAll = List.of(CategoryEnum.values());
            model.addAttribute("categoriesAll", categoriesAll );
            model.addAttribute("cities", cityService.findAllCities());
            return "event-add";
        }
        event.setUserPlanner(userService.getAuthenticatedUser());
        Event savedEvent = eventService.save(event);
        imageService.saveEventImageFile(savedEvent.getIdEvent(), images);
        return "redirect:/event/myevents";
    }

    @GetMapping("/event/myevents")
    public ModelAndView getMyEvents() {
        User authenticatedUser = userService.getAuthenticatedUser();
        List<Event> enteredEvents = authenticatedUser.getEventsJoined();
        List<Event> plannedEvents = authenticatedUser.getEventsPlanned();
        Role plannerRole = roleService.findByRoleName(ROLE_EVENT_PLANNER);
        boolean isPlanner = authenticatedUser.getRoles().contains(plannerRole);

        ModelAndView modelAndView = new ModelAndView("event-myevents");
        modelAndView.addObject("user", authenticatedUser);
        modelAndView.addObject("enteredEvents", enteredEvents);
        modelAndView.addObject("plannedEvents", plannedEvents);
        modelAndView.addObject("isPlanner", isPlanner);

        return modelAndView;
    }

    @GetMapping("/event/{idEvent}")
    public String getEventById(@PathVariable("idEvent") Long idEvent,
                               Model model){
        User authenticatedUser = userService.getAuthenticatedUser();
        Event event = eventService.findById(idEvent);
        boolean isJoined = false;
        if(authenticatedUser.getEventsJoined().contains(event)) {
            isJoined = true;
        }
        List<User> peopleJoined = eventService.findPeopleJoined(event);

        model.addAttribute("event", event);
        model.addAttribute("isJoined", isJoined);
        model.addAttribute("peopleJoined", peopleJoined);

        log.info("people who joined {} ", peopleJoined);

        return "event-single";
    }

    @GetMapping("/event/edit/{idEvent}")
    public ModelAndView editEventById(@PathVariable("idEvent") Long idEvent) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Event event = eventService.findById(idEvent);
        if(!authenticatedUser.equals(event.getUserPlanner())) {
            ModelAndView modelAndView = new ModelAndView("access-denied");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("event-edit");
        modelAndView.addObject("event", event);
        List<CategoryEnum> categoriesAll = List.of(CategoryEnum.values());
        modelAndView.addObject("categoriesAll", categoriesAll );
        modelAndView.addObject("cities", cityService.findAllCities());
        return modelAndView;
    }

    @PostMapping("/event/edit")
    public String editEvent(@ModelAttribute @Valid Event event,
                           BindingResult bindingResult,
                           @RequestParam("eventimages") MultipartFile[] images,
                            Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            List<CategoryEnum> categoriesAll = List.of(CategoryEnum.values());
            model.addAttribute("categoriesAll", categoriesAll );
            model.addAttribute("cities", cityService.findAllCities());
            return "event-edit";
        }
        Event dbEvent = eventService.findById(event.getIdEvent());
        event.setUserPlanner(dbEvent.getUserPlanner());
        event.getImages().addAll(dbEvent.getImages());

        Event savedEvent = eventService.save(event);
        if(images.length > 1) {
            imageService.saveEventImageFile(savedEvent.getIdEvent(), images);
        }
        log.info("Successfully edited event with id {}", savedEvent.getIdEvent());
        return "redirect:/event/" + event.getIdEvent();
    }
    @RequestMapping("/event/{idEvent}/join")
    public String joinEvent(@PathVariable("idEvent") Long idEvent,
                           @RequestParam("username") String username) {
        Event event = eventService.findById(idEvent);
        User user = userService.findByUsername(username);

        user.getEventsJoined().add(event);
        User savedUser = userService.saveWithoutHash(user);

        log.info("User {} joined event with id {}", username, idEvent);
        return "redirect:/event/" + idEvent;
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping("/event/delete/{idEvent}")
    public String deletePostById(@PathVariable("idEvent") Long idEvent,
                                 @RequestParam("username") String username){
        eventService.deleteById(idEvent);
        log.info("User {} successfully deleted event with id {}", username, idEvent);
        return "redirect:/event/myevents";
    }

    @RequestMapping("/event/{idEvent}/removeJoin")
    public String removeJoinEvent(@PathVariable("idEvent") Long idEvent,
                            @RequestParam("username") String username) {
        Event event = eventService.findById(idEvent);
        User user = userService.findByUsername(username);

        if(user.getEventsJoined().contains(event)) {
            user.getEventsJoined().remove(event);
            User savedUser = userService.saveWithoutHash(user);
        } else {
            throw new NotFoundException("Join not found!");
        }

        log.info("User {} remove join from event with id {}", username, idEvent);
        return "redirect:/event/" + idEvent;
    }

    @RequestMapping("/event/recommendations/{username}")
    public ModelAndView recommendEvents(@PathVariable("username") String username) {
        List<Event> allEvents = eventService.findAll();
        User user = userService.findByUsername(username);
        List<Event> recommendations = eventRecommender.generateEventRecommendations(user.getIdUser(), 5, allEvents);

        ModelAndView modelAndView = new ModelAndView("event-recommendations");
        modelAndView.addObject("recommendations", recommendations);

        return modelAndView;

    }




}
