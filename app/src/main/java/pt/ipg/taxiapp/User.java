package pt.ipg.taxiapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String Nome;
    private String Email;
    private String Foto;
    private int Star;

    public User(String nome, String email, String foto, int star) {
        Nome = nome;
        Email = email;
        Foto = foto;
        Star = star;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return Nome;
    }

    public String getEmail() {
        return Email;
    }

    public String getFoto() {
        return Foto;
    }

    public int getStar() {
        return Star;
    }
}
