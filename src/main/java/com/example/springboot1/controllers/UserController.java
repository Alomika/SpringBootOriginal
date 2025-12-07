package com.example.springboot1.controllers;

import com.example.springboot1.model.*;
import com.example.springboot1.repositories.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BasicUserRepo basicUserRepository;
    @Autowired
    private RestaurantRepo restaurantRepository;
    @Autowired
    private DriverRepo driverRepository;
    @Autowired
    private CuisineRepo cuisineRepo;

    @GetMapping("/allUsers")
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAllUsers()).withRel("allUsers")))
                .collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    @PostMapping(value = "/validateUser", consumes = "application/json")
    public @ResponseBody User getUserByCredentials(@RequestBody LoginRequest request) {
        return userRepository.getUserByLoginAndPassword(request.getLogin(), request.getPassword());
    }

    @GetMapping("/user/{id}")
    public EntityModel<User> getUserById(@PathVariable int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("allUsers"));
    }

    @PostMapping("/insertUser")
    public EntityModel<User> createUser(@RequestBody User user) {
        userRepository.save(user);
        User saved = userRepository.getUserByLoginAndPassword(user.getLogin(), user.getPassword());
        return EntityModel.of(saved,
                linkTo(methodOn(UserController.class).getUserById(saved.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("allUsers"));
    }

    @PutMapping("/updateUserById/{id}")
    public EntityModel<User> updateUserById(@RequestBody String info, @PathVariable int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Gson gson = new Gson();
        Properties properties = gson.fromJson(info, Properties.class);
        user.setName(properties.getProperty("name"));
        user.setSurname(properties.getProperty("surname"));
        user.setPhoneNumber(properties.getProperty("phoneNumber"));
        user.setDateUpdated(LocalDateTime.parse(properties.getProperty("dateUpdated")));
        user.setAdmin(Boolean.parseBoolean(properties.getProperty("isAdmin")));
        userRepository.save(user);
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("allUsers"));
    }

    @DeleteMapping("/deleteUser/{id}")
    public EntityModel<String> deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        String message = userRepository.existsById(id) ? "fail on delete" : "deleted successfully";
        return EntityModel.of(message, linkTo(methodOn(UserController.class).getAllUsers()).withRel("allUsers"));
    }

    @GetMapping("/allCustomers")
    public CollectionModel<EntityModel<BasicUser>> getAllCustomers() {
        List<EntityModel<BasicUser>> users = basicUserRepository.findAll().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getBasicUserById(user.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(users);
    }

    @GetMapping("/basicUser/{id}")
    public EntityModel<BasicUser> getBasicUserById(@PathVariable int id) {
        BasicUser user = basicUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "BasicUser not found"));
        return EntityModel.of(user, linkTo(methodOn(UserController.class).getBasicUserById(id)).withSelfRel());
    }

    @PostMapping("/insertBasic")
    public EntityModel<BasicUser> createBasicUser(@RequestBody BasicUser user) {
        basicUserRepository.save(user);
        BasicUser saved = basicUserRepository.getBasicUserByLoginAndPassword(user.getLogin(), user.getPassword());
        return EntityModel.of(saved, linkTo(methodOn(UserController.class).getBasicUserById(saved.getId())).withSelfRel());
    }

    @GetMapping("/allDrivers")
    public CollectionModel<EntityModel<Driver>> getAllDrivers() {
        List<EntityModel<Driver>> drivers = driverRepository.findAll().stream()
                .map(driver -> EntityModel.of(driver,
                        linkTo(methodOn(UserController.class).getDriverById(driver.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return CollectionModel.of(drivers);
    }

    @GetMapping("/driver/{id}")
    public EntityModel<Driver> getDriverById(@PathVariable int id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
        return EntityModel.of(driver, linkTo(methodOn(UserController.class).getDriverById(id)).withSelfRel());
    }

    @PostMapping("/insertDriver")
    public EntityModel<Driver> createDriver(@RequestBody Driver driver) {
        driverRepository.save(driver);
        Driver saved = driverRepository.getDriverByLoginAndPassword(driver.getLogin(), driver.getPassword());
        return EntityModel.of(saved, linkTo(methodOn(UserController.class).getDriverById(saved.getId())).withSelfRel());
    }

    @GetMapping("/allRestaurants")
    public Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/restaurant/{id}/cuisines")
    public List<Cuisine> getCuisinesByRestaurantId(@PathVariable int id) {
        List<Cuisine> cuisines = cuisineRepo.findCuisineByRestaurantId(id);
        if (cuisines.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cuisines found for restaurant id " + id);
        }
        return cuisines;
    }

    @GetMapping("/restaurant/{id}")
    public EntityModel<Restaurant> getRestaurantById(@PathVariable int id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        return EntityModel.of(restaurant, linkTo(methodOn(UserController.class).getRestaurantById(id)).withSelfRel());
    }
}
