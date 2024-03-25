package com.user.service.userservice.Services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import com.user.service.userservice.Controllers.UserController;
import com.user.service.userservice.Models.UserModel;
import com.user.service.userservice.Repositories.IUserRepository;

// import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
// import io.opentelemetry.api.trace.Tracer;
// import io.opentelemetry.context.Context;
// import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

@Service
public class UserService {

    // Tracer tracer;

    @Autowired
    IUserRepository userRepository;
    // UserService(OpenTelemetry openTelemetry) {
    //     tracer = openTelemetry.getTracer(UserService.class.getName(), "0.1.0");
    // }

    public ArrayList<UserModel> getUsers(){
        // Span span = tracer.spanBuilder("getUsers").setParent(Context.current()).startSpan();
        Span span = Span.current();
        try {
            
            return (ArrayList<UserModel>) userRepository.findAll();

        } finally {
            span.end();
        }
    }

    public UserModel saveUser(UserModel user){
        // Span span = tracer.spanBuilder("saveUsers").setParent(Context.current()).startSpan();
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserEmail", user.getEmail());
            
            return userRepository.save(user);

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    public Optional<UserModel> getById(Long id){
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()){
            span.setAttribute("body.UserId", id);

            return userRepository.findById(id);
            
        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    public UserModel updateById(UserModel request, Long id){
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserId", id);
            
            UserModel user = userRepository.findById(id).get();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            return user;

        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }

    }

    public Boolean deleteUser (Long id){    
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserId", id);
            
            try {
                userRepository.deleteById(id);
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
