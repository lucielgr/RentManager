package com.epf.rentmanager.model;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

public class Client {
    private long id;
    private String nom;
    private String prenom;
    private LocalDate naissance;
    private String email;
    @Autowired
    private ClientService clientService;

    public Client(long id, String nom, String prenom, LocalDate dateNaissance, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.naissance = dateNaissance;
        this.email = email;
    }

    public Client(String nom, String prenom, LocalDate naissance, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.naissance = naissance;
        this.email = email;
    }

    public Client() {

    }

    public long getId() {return id;}
    public String getNom() {return nom;}
    public String getPrenom() {return prenom;}
    public LocalDate getNaissance() {return naissance;}
    public String getEmail() {return email;}

    public void setId(long id) {this.id = id;}
    public void setNom(String nom) {this.nom = nom;}
    public void setPrenom(String prenom) {this.prenom = prenom;}
    public void setNaissance(LocalDate naissance) {this.naissance = naissance;}
    public void setEmail(String email) {this.email = email;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id && nom.equals(client.nom) && prenom.equals(client.prenom) && naissance.equals(client.naissance) && email.equals(client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, naissance, email);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + naissance +
                ", email='" + email + '\'' +
                '}';
    }

    public boolean isLegal(){
        LocalDate today = LocalDate.now();
        int age =  Period.between(naissance, today).getYears();
        return(age>=18);
    }

    public boolean isUnique(List<Client> allClients){
        boolean result = true;
            for(Client client : allClients){
                String mailClient = client.getEmail();
                if(this.email.equals(mailClient)){
                    result = false;
                }
            }
        return result;
    }



}
