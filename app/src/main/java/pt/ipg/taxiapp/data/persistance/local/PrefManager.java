package pt.ipg.taxiapp.data.persistance.local;

import android.content.Context;
import android.content.SharedPreferences;

import pt.ipg.taxiapp.data.model.LoginResponse;
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
