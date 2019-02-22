package pt.ipg.taxiapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.maps.model.LatLng;

import java.io.Serializable;

@Entity(tableName = "booking_table")
public class Booking implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    // manter isto public só para ver a diferença na implementação
    public String status;
    public String date;
    public String pickup;
    public String destination;
    public String time;
    public String ride_class;
    public String payment;
    public String fare;
    public String booking_code;

    @Ignore
    public LatLng origem;  // ignore em room ..não preciso guarda e é mais complexo fazer conversão
    @Ignore
    public LatLng destino; // ignore em room ..não preciso guarda e é mais complexo fazer conversão

    public String origem_string;
    public String destino_string;

    // Taxi--- Guarda assim paraser mais simples no ROOMM
    public String matriculaTaxi;
    public String modeloTaxi;
    public String condutorTaxi;
    public String imageTaxi;


    // criar construtor para guardar histórico


    public Booking(String status, String date, String pickup, String destination, String time, String ride_class, String payment, String fare, String booking_code, String origem_string, String destino_string, String matriculaTaxi, String modeloTaxi, String condutorTaxi, String imageTaxi) {
        this.status = status;
        this.date = date;
        this.pickup = pickup;
        this.destination = destination;
        this.time = time;
        this.ride_class = ride_class;
        this.payment = payment;
        this.fare = fare;
        this.booking_code = booking_code;
        this.origem_string = origem_string;
        this.destino_string = destino_string;

        this.matriculaTaxi = matriculaTaxi;
        this.modeloTaxi = modeloTaxi;
        this.condutorTaxi = condutorTaxi;
        this.imageTaxi = imageTaxi;
    }

    // construtor vazio
    @Ignore
    public Booking() {

    }
    @Ignore
    public Booking(String pickup, String destination, String time, String ride_class, String payment, String fare, String origem_string, String destino_string) {
        this.pickup = pickup;
        this.destination = destination;
        this.time = time;
        this.ride_class = ride_class;
        this.payment = payment;
        this.fare = fare;
        this.origem_string = origem_string;
        this.destino_string = destino_string;
    }

    //------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
