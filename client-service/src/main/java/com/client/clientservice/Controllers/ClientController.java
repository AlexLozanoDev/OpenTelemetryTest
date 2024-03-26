package com.client.clientservice.Controllers;

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

import com.client.clientservice.Models.ClientModel;
import com.client.clientservice.Services.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
// import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ClientService userService;
    // UserController(OpenTelemetry openTelemetry) {
    // tracer = openTelemetry.getTracer(UserController.class.getName(), "0.1.0");
    // }

    @GetMapping
    public ArrayList<ClientModel> getUsers() {
        logger.info("Conultando todos los usuario");
        return this.userService.getUsers();
    }

    @PostMapping
    public ClientModel saveUser(@RequestBody ClientModel user) throws Throwable {
        String requestBodyJson = objectMapper.writeValueAsString(user);
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.request", requestBodyJson);

            logger.info("Consultando usuario");
            ClientModel response = this.userService.saveUser(user);

            String responseBody = objectMapper.writeValueAsString(response);
            span.setAttribute("body.response", responseBody);

            return response;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar el objeto a JSON", e);
        } catch (Throwable e) {
            span.recordException(e);
            span.setStatus(StatusCode.ERROR, e.toString());
            throw e;
        } finally {
            span.end();
        }
    }

    @GetMapping(path = "/{linea}")
    public Optional<ClientModel> getUserByLinea(@PathVariable("linea") String linea) throws Throwable {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("linea: ", linea);

            logger.info("Concultando usuario con linea: " + linea);
            Optional<ClientModel> response = this.userService.getById(linea);
            String responseBody = objectMapper.writeValueAsString(response);
            span.setAttribute("body.response", responseBody);

            return response;

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    @PutMapping(path = "/{linea}")
    public ClientModel updateUserByLinea(@RequestBody ClientModel request, @PathVariable("linea") String linea) throws Throwable {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("linea", linea);
            span.setAttribute("saldo", request.getSaldo());

            logger.info("Haciendo update a usuario con linea: " + linea);
            ClientModel response = this.userService.updateSaldoById(request.getSaldo(), linea);
            
            String bodyResponse = objectMapper.writeValueAsString(response);
            span.setAttribute("body.response", bodyResponse);

            return response;

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    @DeleteMapping(path = "/{linea}")
    public String deleteLinea(@PathVariable("linea") String linea) throws Throwable {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("linea", linea);

            boolean ok = this.userService.deleteUser(linea);
            if (ok) {
                logger.info("Se borro el usuario " + linea);
                String response = "User deleted correctly";
                span.setAttribute("body.response", response);
                return response;
            } else {
                String response = "User deleted correctly";
                span.setAttribute("body.response", response);
                return response;
            }

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }

    }
}
