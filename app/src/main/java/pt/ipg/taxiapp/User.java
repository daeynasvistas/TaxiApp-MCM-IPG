package pt.ipg.taxiapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nome;
    private String email;
    private String foto;
    private int star;

    public User(String nome, String email, String foto, int star) {
        this.nome = nome;
        this.email = email;
        this.foto = foto;
        this.star = star;
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
}
