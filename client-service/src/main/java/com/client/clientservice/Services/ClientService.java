package com.client.clientservice.Services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.user.service.userservice.Controllers.UserController;
import com.client.clientservice.Models.ClientModel;
import com.client.clientservice.Repositories.IClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

// import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
// import io.opentelemetry.api.trace.Tracer;
// import io.opentelemetry.context.Context;
// import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

@Service
public class ClientService {

    // Tracer tracer;
    ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    IClientRepository userRepository;
    // UserService(OpenTelemetry openTelemetry) {
    //     tracer = openTelemetry.getTracer(UserService.class.getName(), "0.1.0");
    // }

    public ArrayList<ClientModel> getUsers(){
        // Span span = tracer.spanBuilder("getUsers").setParent(Context.current()).startSpan();
        Span span = Span.current();
        try {
            
            return (ArrayList<ClientModel>) userRepository.findAll();

        } finally {
            span.end();
        }
    }

    public ClientModel saveUser(ClientModel user) throws Throwable{
        // Span span = tracer.spanBuilder("saveUsers").setParent(Context.current()).startSpan();
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            String  requestBodyJson = objectMapper.writeValueAsString(user);
            span.setAttribute("body.request", requestBodyJson);
            
            ClientModel response = userRepository.save(user);

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

    public Optional<ClientModel> getById(String linea) throws Throwable{
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()){
            span.setAttribute("linea", linea);

            Optional<ClientModel> response = userRepository.findById(linea);

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

    public ClientModel updateSaldoById(Double saldo, String linea) throws Throwable{
        Span span = Span.current();
        ClientModel user;
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("linea", linea);
            
            Optional<ClientModel> clientOptional = userRepository.findById(linea);
            if (clientOptional.isPresent()) {
                ClientModel client = clientOptional.get();
                client.setSaldo(saldo);
                user = userRepository.save(client);
            }else{
                user = null;
            }

            String bodyResponse = objectMapper.writeValueAsString(user);
            span.setAttribute("body.response", bodyResponse);

            return user;

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }

    }

    public Boolean deleteUser (String linea){    
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("linea", linea);
            
            try {
                userRepository.deleteById(linea);
                return true;
            } catch (Exception e) {
                return false;
            }

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }

    }
}
