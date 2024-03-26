package com.reservation.service.reservationservice.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.reservation.service.reservationservice.Models.RevervationModel;
import com.reservation.service.reservationservice.Models.TableModel;
import com.reservation.service.reservationservice.Models.UserModel;
import com.reservation.service.reservationservice.Repositories.IReservationRepository;

// import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
// import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@Service
public class ReservationService {

    @Autowired
    IReservationRepository reservationRepository;

    private final String usersApiUrl = "http://localhost:8081";
    private final String tablesApiUrl = "http://localhost:8082";
    private final RestTemplate restTemplate = new RestTemplate();

    //private final Tracer tracer = GlobalOpenTelemetry.getTracer("Intrumentation name", "1.0");

    public RevervationModel makeReservation(RevervationModel reservation) {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserId", reservation.getUserId());
            span.setAttribute("body.TableId", reservation.getTableId());
            span.setAttribute("body.LocalTime", reservation.getdLocalDateTime().toString());

            UserModel user = getUserById(reservation.getUserId());
            if (user == null) {
                user = registerUser();
            }

            TableModel table = getTableById(reservation.getTableId());
            if (table != null && table.getAvailable()) {
                reservation.setUserId(user.getId());
                updateTableAvailability(table.getId());
                return reservationRepository.save(reservation);
            } else {
                return null;
            }

        } catch (Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }
    }

    public ArrayList<RevervationModel> getAllReservations() {
        return (ArrayList<RevervationModel>) reservationRepository.findAll();
    }

    public Optional<RevervationModel> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    private UserModel getUserById(Long id) {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.UserId", id);
            return restTemplate.getForObject(usersApiUrl + "/user/" + id, UserModel.class);
        } catch (Throwable t) {
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }
    }

    private UserModel registerUser() {
        Span span = Span.current();
        try {
            return restTemplate.postForObject(usersApiUrl + "/user", new UserModel(), UserModel.class);
        } catch (Throwable t) {
            span.recordException(t);
            throw t;
        } finally{
            span.end();
        }
    }

    private TableModel getTableById(Long id) {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("body.TableId", id);
        return restTemplate.getForObject(tablesApiUrl + "/table/" + id, TableModel.class);
        } catch (Throwable t){
            span.recordException(t);
            throw t;
        } finally {
            span.end();
        }
    }

    private void updateTableAvailability(Long tableId) {
        Span span = Span.current();
        try (Scope scope = span.makeCurrent()){
            span.setAttribute("body.TableId", tableId);
            
            String requestUrl = tablesApiUrl + "/table/" + tableId;
            java.util.Map<String, Boolean> requestBody = new HashMap<>();
            requestBody.put("available", false);
            restTemplate.put(requestUrl, requestBody);
            
        } catch (Throwable e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
