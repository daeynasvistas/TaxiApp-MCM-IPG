package pt.ipg.taxiapp.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;


import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.ui.authentication.LoginActivity;
import pt.ipg.taxiapp.ui.authentication.MainEmptyActivity;
import pt.ipg.taxiapp.ui.main.MainActivity;
import pt.ipg.taxiapp.utils.Tools;

public class ActivitySplash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        // aqui : https://android.jlelse.eu/launch-screen-in-android-the-right-way-aca7e8c31f52
        setTheme(R.style.SplashTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(ActivitySplash.this, MainEmptyActivity.class);
                startActivity(mainIntent);
                finish();

               /* Intent i = new Intent(ActivitySplash.this, MainActivity.class);
                startActivity(i);
                finish();*/
            }
        };
        new Timer().schedule(task, 1000); // simulação ------------------------ REMOVER!!!!

        // for system bar in lollipop
        Tools.systemBarLolipop(this); // MELHORAR .. REVER!!!!!
    }
}
