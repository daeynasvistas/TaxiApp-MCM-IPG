package pt.ipg.taxiapp.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "taxi_table")
public class Taxi {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String email;
    private String foto;
    private int star;
    private Double lat; // Melhor metodo até aqora .. unificar lat e long
    private Double lng;



    public Taxi(String nome, String email, String foto, int star, Double lat, Double lng) {
        this.nome = nome;
        this.email = email;
        this.foto = foto;
        this.star = star;
        this.lat = lat;
        this.lng = lng;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() {
        return foto;
    }

    public int getStar() {
        return star;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}