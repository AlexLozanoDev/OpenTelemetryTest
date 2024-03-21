package com.user.service.userservice.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.userservice.Models.UserModel;
import com.user.service.userservice.Services.UserService;

// import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
// import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    // Tracer tracer;

    @Autowired
    private UserService userService;
    // UserController(OpenTelemetry openTelemetry) {
    // tracer = openTelemetry.getTracer(UserController.class.getName(), "0.1.0");
    // }

    @GetMapping
    public ArrayList<UserModel> getUsers() {
        logger.info("Se consulto todos los usuario");
        return this.userService.getUsers();
    }

    @PostMapping
    public UserModel saveUser(@RequestBody UserModel user) {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserEmail", user.getEmail());

            logger.info("Se creo un nuevo usuario");
            return this.userService.saveUser(user);

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    @GetMapping(path = "/{id}")
    public Optional<UserModel> getUserById(@PathVariable("id") Long id) {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserId", id);

            logger.info("Se obtuvo el usuario con id: " + id);
            return this.userService.getById(id);

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    @PutMapping(path = "/{id}")
    public UserModel updateUserById(@RequestBody UserModel request, @PathVariable("id") Long id) {

        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserId", id);
            
            logger.info("Se realizó un update al usuario " + id);
            return this.userService.updateById(request, id);

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    @DeleteMapping(path = "/{id}")
    public String deleteById(@PathVariable("id") Long id) {

        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserId", id);
            
            boolean ok = this.userService.deleteUser(id);
            if (ok) {
                logger.info("Se borro el usuario " + id);
                return "User deleted correctly";
            } else {
                return "Error, user wasnt deleted";
            }

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }

    }
}
