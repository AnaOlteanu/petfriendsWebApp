package com.example.petfriends.controller;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.EventPlannerRequest;
import com.example.petfriends.model.Pet;
import com.example.petfriends.model.Post;
import com.example.petfriends.model.User;
import com.example.petfriends.model.dto.UserPetFormDto;
import com.example.petfriends.model.mapper.UserPetFormMapper;
import com.example.petfriends.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserPetFormMapper userPetFormMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private PostService postService;


    @GetMapping({"/", "/index"})
    public String getWelcomePage(){
        return "index";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("userPet", new UserPetFormDto());
        model.addAttribute("cities", cityService.findAllCities());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userPet") UserPetFormDto userPetFormDto,
                               BindingResult bindingResult,
                               @RequestParam("userimages")MultipartFile[] userImages,
                               @RequestParam("petimages")MultipartFile[] petImages
                               ) throws IOException {

        log.info("Binding results ", bindingResult.hasErrors());

        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = userPetFormMapper.userPetFormDtoToUser(userPetFormDto);
        Pet pet = userPetFormMapper.userPetFormDtoToPet(userPetFormDto);

        User savedUser = userService.save(user);
        Pet savedPet = petService.save(pet);
        savedUser.setPet(savedPet);
        imageService.saveUserImageFile(savedUser.getIdUser(), userImages);
        imageService.savePetImageFile(savedPet.getIdPet(), petImages);

        log.info("User with username {} and pet {} has successfully registered on {}",user.getUsername(), pet.getName(), LocalDate.now());

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/user/{username}")
    public ModelAndView getUserByUsername(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        List<Post> posts = postService.findByUserId(user.getIdUser());
        User authenticatedUser = userService.getAuthenticatedUser();
        ModelAndView modelAndView = new ModelAndView("user-profile");
        modelAndView.addObject("posts", posts);
        modelAndView.addObject("user", user);
        modelAndView.addObject("isFollowed", userService.isFollowed(authenticatedUser.getIdUser(), user.getIdUser()));
        log.info("isFollwoed {} ", userService.isFollowed(authenticatedUser.getIdUser(), user.getIdUser()));
        return modelAndView;
    }

    @RequestMapping("/user/search")
    public String searchUser(@RequestParam("searchUserInput") String searchUserInput,
                             Model model) {

        if(searchUserInput == ""){
            model.addAttribute("searchUserResults", null);
            model.addAttribute("searchUserInput", searchUserInput);
            return "post-list::search_results_list";
        }
        List<User> searchResults = userService.findByUsernameSearch(searchUserInput);
        model.addAttribute("searchUserResults", searchResults);
        model.addAttribute("searchUserInput", searchUserInput);
        return "post-list::search_results_list";

    }

    @RequestMapping("/follow/user/{userFollowed}")
    public String followUser(@PathVariable("userFollowed") String userFollowed,
                             @RequestParam("userSource") String userSource) {

        User followedUser = userService.findByUsername(userFollowed);
        User sourceUser = userService.findByUsername(userSource);

        sourceUser.getFollowedUsers().add(followedUser);
        User savedUser = userService.saveWithoutHash(sourceUser);
        log.info("User with username {} follows user {}", sourceUser.getUsername(), followedUser.getUsername());
        return "redirect:/user/" + followedUser.getUsername();
    }

    @RequestMapping("/unfollow/user/{userFollowed}")
    public String unfollowUser(@PathVariable("userFollowed") String userFollowed,
                             @RequestParam("userSource") String userSource) {

        User followedUser = userService.findByUsername(userFollowed);
        User sourceUser = userService.findByUsername(userSource);

        if(userService.isFollowed(sourceUser.getIdUser(), followedUser.getIdUser())){
            sourceUser.getFollowedUsers().remove(followedUser);
            userService.saveWithoutHash(sourceUser);
        } else {
            throw new NotFoundException("Follow not found");
        }

        log.info("User with username {} unfollowed user {}", sourceUser.getUsername(), followedUser.getUsername());
        return "redirect:/user/" + followedUser.getUsername();
    }





}