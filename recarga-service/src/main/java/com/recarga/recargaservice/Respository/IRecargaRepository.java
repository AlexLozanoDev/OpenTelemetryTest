package com.recarga.recargaservice.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recarga.recargaservice.Models.RecargaModel;

@Repository
public interface IRecargaRepository extends JpaRepository<RecargaModel, Long> {

}
