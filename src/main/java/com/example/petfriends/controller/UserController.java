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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
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
                           @RequestParam("petimages")MultipartFile[] petImages,
                           Model model) throws IOException {

        log.info("Binding results ", bindingResult.hasErrors());

        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", cityService.findAllCities());
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
        Long numberFollowing = (long) user.getFollowedUsers().size();
        Long numberFollowers = userService.getNumberFollowers(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView modelAndView = new ModelAndView("user-profile");
        log.info("autoritate {} ", authentication.getAuthorities().contains("ROLE_ADMIN"));
        if(authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))){

            modelAndView.addObject("posts", posts);
            modelAndView.addObject("user", user);
            modelAndView.addObject("isFollowed", null);
            modelAndView.addObject("numberFollowers", numberFollowers);
            modelAndView.addObject("numberFollowing", numberFollowing);
            return modelAndView;
        }

        User authenticatedUser = userService.getAuthenticatedUser();

        modelAndView.addObject("posts", posts);
        modelAndView.addObject("user", user);
        modelAndView.addObject("isFollowed", userService.isFollowed(authenticatedUser.getIdUser(), user.getIdUser()));
        modelAndView.addObject("numberFollowers", numberFollowers);
        modelAndView.addObject("numberFollowing", numberFollowing);

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

    @GetMapping("/followers/{username}")
    public ModelAndView getFollowers(@PathVariable("username") String username){

        User user = userService.findByUsername(username);
        List<User> followers = userService.getFollowers(user.getIdUser());
        ModelAndView modelAndView = new ModelAndView("users-list");
        modelAndView.addObject("users", followers);
        modelAndView.addObject("followers", true);

        return modelAndView;
    }

    @GetMapping("/following/{username}")
    public ModelAndView getFollowings(@PathVariable("username") String username){

        User user = userService.findByUsername(username);
        List<User> following = user.getFollowedUsers();
        ModelAndView modelAndView = new ModelAndView("users-list");
        modelAndView.addObject("users", following);
        modelAndView.addObject("following", true);

        return modelAndView;
    }





}
