package com.recarga.recargaservice.Controllers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recarga.recargaservice.Models.RecargaModel;
import com.recarga.recargaservice.Services.RecargaService;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;

@RestController
@RequestMapping("/recarga")
public class RecargaController {

    private static final Logger logger = LoggerFactory.getLogger(RecargaController.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RecargaService recargaService;

    @GetMapping
    public ArrayList<RecargaModel> getRecargas(){
        
        logger.info("Consultando recargas");
        return recargaService.getRecargas();
    }

    @PostMapping
    public RecargaModel newRecarga(@RequestBody RecargaModel recargaModel) throws Throwable{

        Span span = Span.current();

        try (Scope scope = span.makeCurrent()){
            String bodyRequest = objectMapper.writeValueAsString(recargaModel);
            span.setAttribute("body.request", bodyRequest);

            logger.info("Haciendo una recarga");
            RecargaModel response = this.recargaService.newRecarga(recargaModel);

            String responseBody = objectMapper.writeValueAsString(response);
            logger.info("Response Body;", responseBody);
            span.setAttribute("body.response", responseBody);

            return response;
            
        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
