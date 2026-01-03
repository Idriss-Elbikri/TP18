package com.example.grpc2.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "comptes")
public class Compte {

    @Id
    private String id;

    private double solde;

    private String dateCreation;

    @Enumerated(EnumType.STRING)
    private TypeCompteEntity type;

    public Compte() {
        this.id = UUID.randomUUID().toString();
    }

    public Compte(String id, double solde, String dateCreation, TypeCompteEntity type) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.solde = solde;
        this.dateCreation = dateCreation;
        this.type = type;
    }

    public String getId() { return id; }
    public double getSolde() { return solde; }
    public String getDateCreation() { return dateCreation; }
    public TypeCompteEntity getType() { return type; }

    public void setId(String id) { this.id = id; }
    public void setSolde(double solde) { this.solde = solde; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }
    public void setType(TypeCompteEntity type) { this.type = type; }
}
