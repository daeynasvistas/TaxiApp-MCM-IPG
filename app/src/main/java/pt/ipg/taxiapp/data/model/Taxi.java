package pt.ipg.taxiapp.data.model;

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
    private String lat; // Melhor metodo até aqora .. unificar lat e long
    private String lng;


    public Taxi(int id, String nome, String email, String foto, int star, String lat, String lng) {
        this.id = id;
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

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
