package com.table.service.tableservice.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mesas")
public class TableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer capacity;

    @Column
    private Boolean available;

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    public Integer getCapacity() {
        return capacity;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
    public Boolean getAvailable() {
        return available;
    }

}
