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





@Service
public class ReservationService {

    @Autowired
    IReservationRepository reservationRepository;

    private final String usersApiUrl = "http://localhost:8081";
    private final String tablesApiUrl = "http://localhost:8082";
    private final RestTemplate restTemplate = new RestTemplate();



    public RevervationModel makeReservation(RevervationModel reservation) {

        
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
    }

    public ArrayList<RevervationModel> getAllReservations() {
        return (ArrayList<RevervationModel>) reservationRepository.findAll();
    }

    public Optional<RevervationModel> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    
    private UserModel getUserById(Long id) {
        return restTemplate.getForObject(usersApiUrl + "/user/" + id, UserModel.class);
    }

    private UserModel registerUser() {
        return restTemplate.postForObject(usersApiUrl + "/user", new UserModel(), UserModel.class);
    }

    
    private TableModel getTableById(Long id) {
        return restTemplate.getForObject(tablesApiUrl + "/table/" + id, TableModel.class);
    }

    private void updateTableAvailability(Long tableId) {
        String requestUrl = tablesApiUrl + "/table/" + tableId;
        java.util.Map<String, Boolean> requestBody = new HashMap<>();
        requestBody.put("available", false);
        restTemplate.put(requestUrl, requestBody);
    }
}
