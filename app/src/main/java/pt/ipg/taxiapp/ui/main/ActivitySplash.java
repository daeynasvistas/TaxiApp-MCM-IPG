package pt.ipg.taxiapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;


import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.utils.Tools;

public class ActivitySplash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
                startActivity(i);
                finish();
            }
        };
        new Timer().schedule(task, 2000); // simulação ------------------------ REMOVER!!!!

        // for system bar in lollipop
        Tools.systemBarLolipop(this); // MELHORAR .. REVER!!!!!
    }
}
