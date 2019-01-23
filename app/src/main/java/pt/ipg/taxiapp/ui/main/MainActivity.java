package pt.ipg.taxiapp.ui.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.adapter.RideAdapter;
import pt.ipg.taxiapp.data.Constant;
import pt.ipg.taxiapp.data.model.Ride;
import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.model.TaxiPosition;
import pt.ipg.taxiapp.utils.CurrentLocationListener;
import pt.ipg.taxiapp.utils.Tools;

public class MainActivity extends AppCompatActivity {
    private TaxiViewModel taxiViewModel;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private GoogleMap mMap;
    private Polyline polyline;

    private StringBuilder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // vers0.1
        initToolbar();
        initDrawerMenu();

        //Vers0.2
        initMapFragment();

        //Vers0.2
        initComponent();

        //Vers0.5
        initLocation();

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
                    Log.d(MainActivity.class.getSimpleName(),
                            "Location Changed " + location.getLatitude() + " : " + location.getLongitude());
                   //  Toast.makeText(MainActivity.this, "Location Changed", Toast.LENGTH_SHORT).show();

                    builder.setLength(0); // TEST DEBUD .. manter append para guardar rota do utilizado r(se necessário)
                    builder.append(location.getLatitude()).append(" : ").append(location.getLongitude()).append("\n");

                    Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_LONG).show();
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


    private static void displayMarker(Activity act, GoogleMap googleMap, TaxiPosition c) {
        // make current location marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(c.latLng);
        markerOptions.anchor(0.5f, 0.5f);

        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marker_view = inflater.inflate(R.layout.maps_marker, null);
        ImageView marker = (ImageView) marker_view.findViewById(R.id.marker);
        marker.setImageResource(R.drawable.ic_car_pin);
        marker.setLayoutParams(new LinearLayout.LayoutParams(Tools.dpToPx(act, 40), Tools.dpToPx(act, 40)));
        marker.setRotation(c.rotation);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(act, marker_view)));
        googleMap.addMarker(markerOptions);
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
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(
                new com.google.android.gms.maps.model.LatLng(40.777198, -7.350320), 15);

        mMap.moveCamera(center);
        // RECEBER da Minha Posição !!!! <------------------------ ver 0.3 ------------ ToDO

        taxiViewModel = ViewModelProviders.of(this).get(TaxiViewModel.class);
        taxiViewModel.getAllTaxis().observe(this, new Observer<List<Taxi>>() {
            @Override
            public void onChanged(@Nullable List<Taxi> taxis) {
                //    Tools.displayCarAroundMarkers(MainActivity.this, mMap);
                // update o mapa com novos Taxis .. Vers.0.4
                int size = taxiViewModel.getAllTaxis().getValue().size();
                Tools.showToastMiddle(getApplicationContext(), Integer.toString(size));

                List<TaxiPosition> items = Constant.getTaxiArounddb(taxis);
                mMap.clear();
                LatLng origin = new LatLng(40.777570, -7.349922);
                LatLng destination = new LatLng(40.827570, -7.349922);

                displayMarker(origin, true);
                displayMarker(destination, false);
                drawPolyLine(origin, destination);

                for (TaxiPosition c : items) {
                     displayMarker(MainActivity.this, mMap, c);
                 }

            }
        });

       // Tools.displayCarAroundMarkers(this, mMap);
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

        ((View) findViewById(R.id.lyt_ride)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRideClass();
            }
        });

        ((View) findViewById(R.id.lyt_request_ride)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), ActivityRequestRide.class));
                com.google.android.gms.maps.model.LatLng point = new com.google.android.gms.maps.model.LatLng(40.777570, -7.349922);
                com.google.android.gms.maps.model.LatLng randPin = Tools.getRandomLocation(point,2500);
                taxiViewModel.insert(new Taxi("OK","Daniel@ept.pt","foto(alterar)",0,randPin.latitude,randPin.longitude));

            }
        });
    }









    // ------------ MOVER para viewModel ------------------------ !!!!!!!!!!!!!!!11



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
