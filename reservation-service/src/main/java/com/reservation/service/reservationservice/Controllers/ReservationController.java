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

// import io.opentelemetry.api.GlobalOpenTelemetry;
// import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
// import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ReservationController.class);
    // private final Tracer tracer = GlobalOpenTelemetry.getTracer("Intrumentation name", "1.0"); 

    @Autowired
    private ReservationService reservationService;
    // ReservationController(OpenTelemetry openTelemetry) {
    //     tracer = openTelemetry.getTracer(ReservationController.class.getName(), "0.1.0");
    // }

    @PostMapping
    public RevervationModel makeReservation(@RequestBody RevervationModel revervationModel) {
        Span span = Span.current();
        // Span span = tracer.spanBuilder("POST RESERVATIONS").startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserId", revervationModel.getUserId());
            span.setAttribute("body.TableId", revervationModel.getTableId());
            span.setAttribute("body.LocalTime", revervationModel.getdLocalDateTime().toString());
            RevervationModel createdReservation = reservationService.makeReservation(revervationModel);
            
            if (createdReservation != null) {
                LOGGER.info("Se creo una nueva reservacion con id: " + createdReservation.getId());
                return createdReservation;
            } else {
                LOGGER.info("No se pudo hacer la reservacion, la mesa esta ocupada");
                return null;
            }
        } catch (Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }
    }

    @GetMapping
    public ArrayList<RevervationModel> getReservationById() {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("BODY", "HOLAAA");
            LOGGER.info("Se consulto la lista de reservaciones");
            return this.reservationService.getAllReservations();
        } catch (Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }

    }

    @GetMapping("/{id}")
    public Optional<RevervationModel> getReservationById(@PathVariable("id") Long id) {
        LOGGER.info("Se consulto la reservacion con id: " + id);
        return this.reservationService.getReservationById(id);
    }

}
