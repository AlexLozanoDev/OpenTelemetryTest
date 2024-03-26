package com.recarga.recargaservice.Models;

public class ClientModel {

    private String linea;

    private String firstName;

    private String lastName;

    private String email;

    private Double saldo;

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getLinea() {
        return linea;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getSaldo() {
        return saldo;
    }
    
    
}
