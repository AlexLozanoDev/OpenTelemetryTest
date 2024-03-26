package com.client.clientservice.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.client.clientservice.Models.ClientModel;

@Repository
public interface IClientRepository extends JpaRepository<ClientModel, String>{

}
