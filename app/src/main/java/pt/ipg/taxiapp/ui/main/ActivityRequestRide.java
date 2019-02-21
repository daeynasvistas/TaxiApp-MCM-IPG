package pt.ipg.taxiapp.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.utils.Tools;


public class ActivityRequestRide extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;
    public static final String EXTRA_OBJECT = "extra.data.BOOKING_OBJ";


    // give preparation animation activity transition
    public static void navigate(Activity activity, Booking obj) {
        Intent intent = new Intent(activity, ActivityRequestRide.class);
        intent.putExtra(EXTRA_OBJECT, obj);
        activity.startActivity(intent);
    }

    private Booking booking;
    private BookingViewModel bookingViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        // get extra object
        booking = (Booking) getIntent().getSerializableExtra(EXTRA_OBJECT);

        initComponent();
        // todo fazer lado do taxi vers 1.0
        scheduleRequest();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initComponent() {
        Animation pulse1 = AnimationUtils.loadAnimation(this, R.anim.pulse);
        ((ImageView) findViewById(R.id.image1)).startAnimation(pulse1);

        Animation pulse2 = AnimationUtils.loadAnimation(this, R.anim.pulse2);
        pulse2.setStartOffset(500);
        ((ImageView) findViewById(R.id.image2)).startAnimation(pulse2);

        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });

    }

    private void showConfirmDialog() {
        handler.removeCallbacks(runnable);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Confirmacao);
        builder.setMessage(R.string.Cancelar_busca);
        builder.setPositiveButton(R.string.Sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Tools.showToastMiddle(getApplicationContext(), getString(R.string.Busca_cancelada));
                finish();
            }
        });
        builder.setNegativeButton(R.string.Nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scheduleRequest();
            }
        });
        builder.show();
    }

    public void showFoundRide() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_found_driver);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((Button) dialog.findViewById(R.id.bt_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // receber taxi que aceitou a viagem
                String matriculaTaxi = "00-AA-00";
                String modeloTaxi = "Audi A4 preto";
                String condutorTaxi = "Cristiano Ronaldo";
                String imageTaxi = "profile.png";
                // criar booking historico
                // Todo --- alterar Status, String não parece correcto -- vers 0.8
                // Todo ----alterar económico, van para enum -- vers 0.8
                // guardar booking ACTIVE em room
                bookingViewModel = ViewModelProviders.of(ActivityRequestRide.this).get(BookingViewModel.class);
                Booking newBooking = new Booking(
                        "ACTIVE",
                        booking.date,
                        booking.pickup,
                        booking.destination,
                        booking.time,
                        booking.ride_class,
                        booking.payment,
                        booking.fare,
                        booking.booking_code,
                        booking.origem_string,
                        booking.destino_string,
                        matriculaTaxi,
                        modeloTaxi,
                        condutorTaxi,
                        imageTaxi);

                bookingViewModel.insert(newBooking);
                Tools.showToastMiddle(getApplicationContext(), "Booking inserido em room");


                // enviar booking ... TODO alterar para ler room em ActivityBookingActiveDetail .. não é preciso passar
                Booking obj = booking; // envio
                ActivityBookingActiveDetails.navigate(ActivityRequestRide.this, obj);
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void scheduleRequest() {
        runnable = new Runnable() {
            public void run() {
                showFoundRide();
            }
        };

        // pseudo busca de taxi ...  TODO fazer aplicação do lado do taxi para aceitar ride
        handler.postDelayed(runnable, 5000);
    }
}
