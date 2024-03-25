package com.apiprueba.apiprueba.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apiprueba.apiprueba.Models.UserModel;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long>{

    
}
