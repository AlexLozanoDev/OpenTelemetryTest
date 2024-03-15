package com.reservation.service.reservationservice.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
public class RevervationModel {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dLocalDateTime;

    @Column
    private Long userId;

    @Column
    private Long tableId;

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setdLocalDateTime(LocalDateTime dLocalDateTime) {
        this.dLocalDateTime = dLocalDateTime;
    }
    public LocalDateTime getdLocalDateTime() {
        return dLocalDateTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
    public Long getTableId() {
        return tableId;
    } 
}
