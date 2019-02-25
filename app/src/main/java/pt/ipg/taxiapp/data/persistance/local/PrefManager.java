package pt.ipg.taxiapp.data.persistance.local;

import android.content.Context;
import android.content.SharedPreferences;

import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.data.model.User;

public class PrefManager {
    private static final String SHARED_PREF_NAME = "my_pref";

    private static PrefManager mInstance;
    private Context mCtx;

    private PrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }


    public static synchronized PrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new PrefManager(mCtx);
        }
        return mInstance;
    }


    public void saveUser(User user, String token) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("username", user.getUsername());
        editor.putString("token", token);

        editor.apply();

    }


    public void saveBookingTemp(Booking booking, Context mCtx) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(booking.origem!=null){
            editor.putString("origem_lat_string", String.valueOf(booking.origem.lat));
            editor.putString("origem_lng_string", String.valueOf(booking.origem.lng));
        }
        if(booking.destino!=null)  {
            editor.putString("destino_lat_string", String.valueOf(booking.destino.lat));
            editor.putString("destino_lng_string", String.valueOf(booking.destino.lng));
        }

       editor.putString("time", booking.time);
       editor.putString("fare", booking.fare);

        editor.apply();

    }

    public Booking getBookingTemp() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String origem_lat = sharedPreferences.getString("origem_lat_string", null);
        String origem_lng = sharedPreferences.getString("origem_lng_string", null);
        String origem = origem_lat+","+origem_lng;

        String destino_lat = sharedPreferences.getString("destino_lat_string", null);
        String destino_lng = sharedPreferences.getString("destino_lng_string", null);
        String destino = destino_lat+","+destino_lng;

        return new Booking(
                sharedPreferences.getString("pickup", null),
                sharedPreferences.getString("destination", null),
                sharedPreferences.getString("time", null),
                sharedPreferences.getString("ride_class", null),
                sharedPreferences.getString("payment", null),
                sharedPreferences.getString("fare", null),
                sharedPreferences.getString("origem_string", origem),
                sharedPreferences.getString("destino_string", destino)
        );

    }


    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }


    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("username", null)
        );
    }
    // --- Confuso -- VER melhor Forma guardar Token....... 0.6
    public String haveToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString("token", null);
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
