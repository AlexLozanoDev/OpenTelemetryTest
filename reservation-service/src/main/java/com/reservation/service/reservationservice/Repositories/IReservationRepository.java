package com.reservation.service.reservationservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.service.reservationservice.Models.RevervationModel;

@Repository
public interface IReservationRepository extends JpaRepository<RevervationModel, Long>{

}
