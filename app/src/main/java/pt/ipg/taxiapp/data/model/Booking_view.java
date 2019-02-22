package pt.ipg.taxiapp.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.maps.model.LatLng;

public class Booking_view implements Parcelable {

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


    public LatLng origem;  // ignore em room ..não preciso guarda e é mais complexo fazer conversão
    public LatLng destino; // ignore em room ..não preciso guarda e é mais complexo fazer conversão

    public String origem_string;
    public String destino_string;


    public Booking_view(Parcel in) {
        status = in.readString();
        date = in.readString();
        pickup = in.readString();
        destination = in.readString();
        time = in.readString();
        ride_class = in.readString();
        payment = in.readString();
        fare = in.readString();
        booking_code = in.readString();
        origem_string = in.readString();
        destino_string = in.readString();
    }

    public static final Creator<Booking_view> CREATOR = new Creator<Booking_view>() {
        @Override
        public Booking_view createFromParcel(Parcel in) {
            return new Booking_view(in);
        }

        @Override
        public Booking_view[] newArray(int size) {
            return new Booking_view[size];
        }
    };

    public Booking_view() {

    }

    public Booking_view(String pickup, String destination, String time, String ride_class, String payment, String fare, String origem_string, String destino_string) {
        this.pickup = pickup;
        this.destination = destination;
        this.time = time;
        this.ride_class = ride_class;
        this.payment = payment;
        this.fare = fare;
        this.origem_string = origem_string;
        this.destino_string = destino_string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(date);
        parcel.writeString(pickup);
        parcel.writeString(destination);
        parcel.writeString(time);
        parcel.writeString(ride_class);
        parcel.writeString(payment);
        parcel.writeString(fare);
        parcel.writeString(booking_code);
        parcel.writeString(origem_string);
        parcel.writeString(destino_string);
    }
}
