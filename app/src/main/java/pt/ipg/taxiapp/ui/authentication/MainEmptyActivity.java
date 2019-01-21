package pt.ipg.taxiapp.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import pt.ipg.taxiapp.ui.main.MainActivity;
import pt.ipg.taxiapp.utils.Tools;

// AQUI .. Muito Bom!
//https://android.jlelse.eu/login-and-main-activity-flow-a52b930f8351

public class MainEmptyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;

        // go straight to main if a token is stored
        //https://android.jlelse.eu/login-and-main-activity-flow-a52b930f8351
        if (Tools.getToken() != null) {
            activityIntent = new Intent(this, MainActivity.class);
        } else {
            activityIntent = new Intent(this, LoginActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }
}