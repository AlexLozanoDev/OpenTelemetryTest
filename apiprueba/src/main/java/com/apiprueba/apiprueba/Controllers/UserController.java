package com.apiprueba.apiprueba.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.apiprueba.apiprueba.Models.UserModel;
import com.apiprueba.apiprueba.Services.UserService;

import ch.qos.logback.classic.Logger;


@RestController
@RequestMapping("/user")
public class UserController {

    private final RestTemplate restTemplate;

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    UserController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }
    
    @GetMapping
    public ArrayList<UserModel> getUsers(){
        LOGGER.info("Se obtuvo lista de usuarios");
        return this.userService.getUsers();
    }

    @PostMapping
    public UserModel saveUser (@RequestBody UserModel user){
        LOGGER.info("Se creo un nuevo usuario");
        return this.userService.saveUser(user);
    }

    @GetMapping(path = "/{id}")
    public Optional<UserModel> getUserById(@PathVariable("id") Long id){
        LOGGER.info("Se obtuvo el usuario " + id);
        return this.userService.getById(id);
    }

    @PutMapping(path = "/{id}")
    public UserModel updateUserById(@RequestBody UserModel request, @PathVariable("id") Long id){
        LOGGER.info("Se actualizo el usuario "+id);
        return this.userService.updateById(request, id);
    }

    @DeleteMapping(path = "/{id}")
    public String deleteById(@PathVariable("id") Long id){
        boolean ok = this.userService.deleteUser(id);
        if (ok) {
            LOGGER.info("Se borro el usuario "+id);
            return "User deleted correctly";
        }else{
            return "Error, user wasnt deleted";
        }
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<String> handleConflict(IllegalArgumentException ex){
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
