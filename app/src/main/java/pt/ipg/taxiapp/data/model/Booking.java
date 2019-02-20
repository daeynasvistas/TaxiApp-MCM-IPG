package pt.ipg.taxiapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.maps.model.LatLng;

import java.io.Serializable;

@Entity(tableName = "booking_table")
public class Booking implements Serializable {
    @PrimaryKey(autoGenerate = true) // remover quando receber JSON, usar mongoDb _id
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
    public LatLng origem;  // ignore em room ..nãopreciso guarda eé mais complexo fazer conversão
    @Ignore
    public LatLng destino;

    public String origem_string;
    public String destino_string;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
