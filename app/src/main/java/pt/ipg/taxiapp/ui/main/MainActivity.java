package pt.ipg.taxiapp.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.PlacesApi;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;


import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.adapter.RideAdapter;
import pt.ipg.taxiapp.data.Constant;
import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.data.model.Ride;
import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.model.TaxiPosition;
import pt.ipg.taxiapp.ui.fragment.FragmentDialogLocation;
import pt.ipg.taxiapp.utils.CurrentLocationListener;
import pt.ipg.taxiapp.utils.MapHelper;
import pt.ipg.taxiapp.utils.Tools;


public class MainActivity extends AppCompatActivity {
    private TaxiViewModel taxiViewModel;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private GoogleMap mMap;
    private Polyline polyline;

    private StringBuilder builder;
    private Location myLocation;
    private Location myFistLocation;

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    private TextView tv_note, tv_promo, tv_payment;
    private EditText et_pickup, et_destination;

    private LatLng origem;
    private LatLng destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Vers0.5
        initLocation();

        // vers0.1
        initToolbar();
        initDrawerMenu();

        //Vers0.2
        initMapFragment();

        //Vers0.2
        initComponent();


        /*
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserA adapter = new TaxiAdapter();
       // recyclerView.setAdapter(adapter);
*/
    }


    private void initLocation() {
        builder = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            getLocationUpdates();
        }
    }

    private void getLocationUpdates() {
        CurrentLocationListener.getInstance(getApplicationContext()).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(@Nullable Location location) {
                if (location != null) {

                   // chamar API para receber taxi num raio de 20Km  Vers 0.6 ---- ToDO
                   // Log.d(MainActivity.class.getSimpleName(),
                   //         "Location Changed " + location.getLatitude() + " : " + location.getLongitude());
                   //  Toast.makeText(MainActivity.this, "Location Changed", Toast.LENGTH_SHORT).show();
                   // builder.setLength(0); // TEST DEBUD .. manter append para guardar rota do utilizado r(se necessário)
                   // builder.append(location.getLatitude()).append(" : ").append(location.getLongitude()).append("\n");


                    // ONCHANGE a minha POS
                    if ((myLocation==null)&&(mMap!=null)){ // First time
                        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(
                                new com.google.android.gms.maps.model.LatLng(location.getLatitude(),location.getLongitude()), 15);
                        mMap.moveCamera(center);
                    }
                    myLocation = location; // posição mobile actualizada

                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocationUpdates();
            }
        }
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_very_hard), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Tools.setCompleteSystemBarLight(this);
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        // -- Encontrar melhor método !! <---- 0.4
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.myUsername);
        navUsername.setText(Tools.getUsername(this));
        // ---------- colocar nome utilizador (talvez base de dados)

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                onNavigationItemClick(menuItem);
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void onNavigationItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            /*
            case R.id.nav_booking:
                startActivity(new Intent(this, ActivityBooking.class));
                break;
*/
            case R.id.nav_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About");
                builder.setMessage(getString(R.string.about_text));
                builder.setNeutralButton("OK", null);
                builder.show();
                break;
        }
    }




    // ----------------------------- Ver 0.2 -------------REMOVER para VIEWMODEL--------------------------------------------
    public void configureMap(GoogleMap googleMap) {
        mMap = Tools.configBasicGoogleMap(googleMap);


       // ONCHANGE taxis num raio 30 KM
        taxiViewModel = ViewModelProviders.of(MainActivity.this).get(TaxiViewModel.class);
        taxiViewModel.getAllTaxis().observe(MainActivity.this, new Observer<List<Taxi>>() {
            @Override
            public void onChanged(@Nullable List<Taxi> taxis) {
                //    Tools.displayCarAroundMarkers(MainActivity.this, mMap);
                // update o mapa com novos Taxis .. Vers.0.4
                //int size = taxiViewModel.getAllTaxis().getValue().size();
                //Tools.showToastMiddle(getApplicationContext(), Integer.toString(size));

                //List<TaxiPosition> items = Constant.getTaxiArounddb(taxis);
                List<TaxiPosition> items = Constant.getTaxiArounddb(taxis);  // recebe da base dados room
              //  mMap.clear(); // ALTERAR!!!!! binding xml devia funcionar

                // REVER 0.7 Nãofunciona!!!!!!!! ------------------------------ ToDO
            //    LatLng point = new LatLng(40.777570, -7.349922);
            //    LatLng origin = Tools.getRandomLocation(point,2500);
            //    LatLng destination = Tools.getRandomLocation(point,2500);

                //  drawPolyLine(origin, destination);


                try {
                    LatLng myLoc = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    mMap.addMarker(MapHelper.displayMarker(MainActivity.this, myLoc, true));
                } catch (Exception e) {
                   // e.printStackTrace();
                }
                // chamar API para receber taxi num raio de 20Km  Vers 0.6 ---- ToDO
               Tools.displayCarAroundMarkers(MainActivity.this, mMap, items);



            }
        });


    }

// PROBLEMAS com: E/AndroidRuntime: FATAL EXCEPTION: Rate Limited Dispatcher
    private void drawPolyLine(LatLng origin, LatLng destination) {

     //   GeoApiContext geoApiContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_key));
     //   geoApiContext.setConnectTimeout(2, TimeUnit.SECONDS);
     //   geoApiContext.setQueryRateLimit(3);
/*

       DirectionsApiRequest d = DirectionsApi.newRequest(geoApiContext);
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
                Toast.makeText(MainActivity.this, "Polyline problema", Toast.LENGTH_SHORT).show();
            }
        });

 /*       */

    }






    //--------------------------------------------------------------------------------------------------
    private void initMapFragment() {
        Tools.checkInternetConnection(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                configureMap(googleMap);
            }
        });
    }

    private void initComponent() {

        //Passar para ViewModel vers 0. 6
        tv_note = (TextView) findViewById(R.id.tv_note);
        tv_promo = (TextView) findViewById(R.id.tv_promo);
        tv_payment = (TextView) findViewById(R.id.tv_payment);

        et_pickup = (EditText) findViewById(R.id.et_pickup);
        et_destination = (EditText) findViewById(R.id.et_destination);
        //Passar para ViewModel vers 0. 6



        ((View) findViewById(R.id.lyt_ride)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRideClass();
            }
        });

        ((View) findViewById(R.id.lyt_pickup)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLocation(true);
            }
        });

        ((View) findViewById(R.id.lyt_destination)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLocation(false);
            }
        });



        ((View) findViewById(R.id.lyt_request_ride)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // passar con putextra ou simplesmente assim com um método da class
                Booking obj = new Booking();

                obj.payment = "Dinheiro";
                obj.ride_class = "ECONÓMICO";
                obj.pickup = String.valueOf(et_pickup.getText());
                obj.destination = String.valueOf(et_destination.getText());
                obj.origem = origem;
                obj.destino = destino;


                ActivityRequestRide.navigate(MainActivity.this, obj);

                //  startActivity(new Intent(getApplicationContext(), ActivityRequestRide.class));

                // DEBUG base de dados room local
/*              LatLng point = new LatLng(40.777570, -7.349922);
                LatLng randPin = Tools.getRandomLocation(point,2500);
                taxiViewModel.insert(new Taxi("OK","Daniel@ept.pt","foto(alterar)",0,randPin.lat,randPin.lng));
*/
            }
        });
    }









    // ------------ MOVER para viewModel ------- DEBUG ----------------- !!!!!!!!!!!!!!!11

    private void showDialogLocation(boolean isPickUp) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Fragment previous = getSupportFragmentManager().findFragmentByTag(FragmentDialogLocation.class.getName());

        if (previous != null) transaction.remove(previous);
        transaction.addToBackStack(null);

        final FragmentDialogLocation fragment = new FragmentDialogLocation();
        fragment.setHint(isPickUp ? "Insira origem do trajeto" : "Destino do trajeto");
        fragment.setRequestCode(isPickUp ? 5000 : 6000); // escolher Id como prof carreto indicou



        fragment.setOnCallbackResult(new FragmentDialogLocation.CallbackResult() {
            @Override
            public void sendResult(int requestCode, String loc, LatLng pos) {
              //  LatLng destination = new LatLng(40.827570, -7.349922);
              //  LatLng origin = new LatLng(40.777570, -7.349922);

                if (requestCode == 5000) {
                    et_pickup.setText(loc);
                    origem = pos;
                    // fazer cenas aqui -- toDO colocar origem no mapa
                   // https://stackoverflow.com/questions/25928948/get-lat-lang-from-a-place-id-returned-by-autocomplete-place-api
                    mMap.addMarker(MapHelper.displayMarker(MainActivity.this, pos, false));

                } else if (requestCode == 6000) {
                    et_destination.setText(loc);
                    destino = pos;
                    // fazer cenas aqui -- toDO colocar destino no mapa
                    mMap.addMarker(MapHelper.displayMarker(MainActivity.this, pos, false));
                }

                // guardar trajeto TEMPORÁRIO na base dados local room  --- ToDo --> guardar trajeto DB

            }
        });

        fragment.show(transaction, FragmentDialogLocation.class.getName());
    }


    // ------------ MOVER para viewModel -------------FIM ----------- !!!!!!!!!!!!!!!11







    /// --- alterar o valor da ride
    private void showDialogRideClass() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_ride_class);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //adapter  .. talvez mudar para listview?
        RideAdapter mAdapter = new RideAdapter(this, Constant.getRideData(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RideAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Ride obj, int position) {
                changeRideClass(obj);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    // MOVER TUDO PARA VIEWMODEL!!!! ver 0.5
    private void changeRideClass(Ride obj) {
        Picasso.with(this).load(obj.image).into(((ImageView) findViewById(R.id.image)));
        ((TextView) findViewById(R.id.class_name)).setText(obj.name);
        ((TextView) findViewById(R.id.price)).setText(obj.price);
        ((TextView) findViewById(R.id.pax)).setText(obj.pess);
        ((TextView) findViewById(R.id.duration)).setText(obj.duration);
    }



}
