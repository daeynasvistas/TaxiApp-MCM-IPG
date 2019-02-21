package pt.ipg.taxiapp.ui.main;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.data.Constant;
import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.model.TaxiPosition;
import pt.ipg.taxiapp.data.persistance.dao.BookingDao;
import pt.ipg.taxiapp.utils.MapHelper;
import pt.ipg.taxiapp.utils.Tools;

public class ActivityBookingActiveDetails extends AppCompatActivity {

    private GoogleMap mMap;
    private Polyline polyline;
    private BottomSheetBehavior bottomSheetBehavior;

    public static final String EXTRA_OBJECT = "extra.data.BOOKING_OBJ";

    // give preparation animation activity transition
    public static void navigate(Activity activity, Booking obj) {
        Intent intent = new Intent(activity, ActivityBookingActiveDetails.class);
        intent.putExtra(EXTRA_OBJECT, obj);
        activity.startActivity(intent);
    }

    private Booking booking;
    private BookingViewModel bookingViewModel;
    private TaxiViewModel taxiViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_active_details);

        // get extra object
        booking = (Booking) getIntent().getSerializableExtra(EXTRA_OBJECT);
        // substituir por room database


/* ----- DEBUG ----  APAGAR ------

        taxiViewModel = ViewModelProviders.of(this).get(TaxiViewModel.class);
        taxiViewModel.getAllTaxis().observe(this, new Observer<List<Taxi>>() {
            @Override
            public void onChanged(@Nullable List<Taxi> taxis) {
                Tools.showToastMiddle(getApplicationContext(), "Change");
                try {

                } catch (Exception e) {
                    // e.printStackTrace();
                }

            }
        });
        taxiViewModel.insert(new Taxi("OK","Android@ept.pt","foto(alterar)",0,40.777570, -7.349922));
//------------------  FIM DEBUG APAGAR ------

*/
        //Tools.showToastMiddle(getApplicationContext(), "Booking inserido");
        //--------------------------------------------  insert FIM ---------------------

        initMapFragment();
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tools.setCompleteSystemBarLight(this);
    }


    private void initComponent() {
        TextView status = (TextView) findViewById(R.id.status);
        TextView payment = (TextView) findViewById(R.id.payment);
        TextView ride_class = (TextView) findViewById(R.id.ride_class);
        TextView pickup = (TextView) findViewById(R.id.pickup);
        TextView destination = (TextView) findViewById(R.id.destination);
        TextView fare = (TextView) findViewById(R.id.fare);

        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        final View top_shadow = (View) findViewById(R.id.top_shadow);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setHideable(false);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    top_shadow.setVisibility(View.GONE);
                } else {
                    top_shadow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        payment.setText(booking.payment);
        ride_class.setText(booking.ride_class);
        pickup.setText(booking.pickup);
        destination.setText(booking.destination);
        fare.setText(booking.fare);
    }

    private void initMapFragment() {
        // aqui: https://developers.google.com/maps/documentation/android-sdk/intro
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                configureMap(googleMap);
            }
        });
    }

    public void configureMap(GoogleMap googleMap) {
        mMap = Tools.configBasicGoogleMap(googleMap);

        // reconstruir LntLat a partir das string do booking (problema envio putextra parecelable)   -- Todo rever melhor método vers 0.9
        String[] latlongOrigem =  booking.origem_string.split(",");
        double latitude_origem = Double.parseDouble(latlongOrigem[0]);
        double longitude_origem = Double.parseDouble(latlongOrigem[1]);

        String[] latlongDestino =  booking.destino_string.split(",");
        double latitude_destino = Double.parseDouble(latlongDestino[0]);
        double longitude_destino = Double.parseDouble(latlongDestino[1]);

        CameraUpdate center = CameraUpdateFactory.newLatLng(new com.google.android.gms.maps.model.LatLng(latitude_origem, longitude_origem));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14.2f);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        LatLng origem = new LatLng(latitude_origem, longitude_origem);
        //LatLng origin = booking.origem; //new LatLng(48.842948, 2.318795);   TODO -- problema com envio parceble .. talvez guarda database
        LatLng destino = new LatLng(latitude_destino, longitude_destino);

        displayMarker(origem, true);
        displayMarker(destino, false);

        drawPolyLine(origem, destino);  // todo .. problema com CC na API google ..
        Tools.displaySingleCarAroundMarker(ActivityBookingActiveDetails.this, mMap,origem);  // todo ...  vers 0.8 colocar taxi a aproximar
    }

    private void drawPolyLine(LatLng origin, LatLng destination) {
        GeoApiContext context = new GeoApiContext().setApiKey(getString(R.string.google_maps_key));
        context.setConnectTimeout(10, TimeUnit.SECONDS);
        DirectionsApiRequest d = DirectionsApi.newRequest(context);
        d.origin(origin).destination(destination).mode(TravelMode.DRIVING).alternatives(false);
        d.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                final PolylineOptions polylineOptions = new PolylineOptions().width(10).color(getResources().getColor(R.color.colorAccent)).geodesic(true);
                for (DirectionsRoute d : result.routes) {
                    for (LatLng l : d.overviewPolyline.decodePath()) {
                        polylineOptions.add(new com.google.android.gms.maps.model.LatLng(l.lat, l.lng));
                    }
                }
                // draw polyline
                runOnUiThread(new Runnable() {
                    public void run() {
                        polyline = mMap.addPolyline(polylineOptions);
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {


            }
        });



    }

    private void displayMarker(LatLng location, boolean isOrigin) {
        // make current location marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new com.google.android.gms.maps.model.LatLng(location.lat, location.lng));

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marker_view = inflater.inflate(R.layout.maps_marker, null);
        ImageView marker = (ImageView) marker_view.findViewById(R.id.marker);
        marker.setBackgroundResource(isOrigin ? R.drawable.marker_origin : R.drawable.marker_destination);
        if (isOrigin) {
            marker.setImageResource(R.drawable.ic_origin);
        }

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(this, marker_view)));
        mMap.addMarker(markerOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_activity_booking_active_details, menu);
       // Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_very_hard));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            Tools.showToastMiddle(getApplicationContext(), item.getTitle() + " clicked");
        }
        return super.onOptionsItemSelected(item);
    }
}
