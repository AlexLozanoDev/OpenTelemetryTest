package com.user.service.userservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.service.userservice.Models.UserModel;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long>{

    
}
