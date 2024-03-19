package com.reservation.service.reservationservice.Controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservation.service.reservationservice.Models.RevervationModel;
import com.reservation.service.reservationservice.Services.ReservationService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ReservationController.class);
    Tracer tracer = GlobalOpenTelemetry.getTracer("Instrumentation name", "1.0");


    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public RevervationModel makeReservation(@RequestBody RevervationModel revervationModel) {
        Span span = tracer.spanBuilder("rollTheDice").startSpan();

        span.setAttribute("Body request", revervationModel.getUserId());

        RevervationModel createdReservation = reservationService.makeReservation(revervationModel);
        if (createdReservation != null) {
            LOGGER.info("Se creo una nueva reservacion con id: " + createdReservation.getId());
            span.end();
            return createdReservation;
        } else {
            LOGGER.info("No se pudo hacer la reservacion, la mesa esta ocupada");
            span.end();
            return null;
        }
    }

    @GetMapping
    public ArrayList<RevervationModel> getReservationById() {
        LOGGER.info("Se consulto la lista de reservaciones");
        return this.reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Optional<RevervationModel> getReservationById(@PathVariable("id") Long id) {
        LOGGER.info("Se consulto la reservacion con id: " + id);
        return this.reservationService.getReservationById(id);
    }

}
