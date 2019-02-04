package pt.ipg.taxiapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.data.model.TaxiPosition;
import pt.ipg.taxiapp.ui.fragment.FragmentDialogLocation;
import pt.ipg.taxiapp.ui.main.MainActivity;

public class MapHelper {



    public static void displayMarker(Activity act, GoogleMap googleMap, TaxiPosition c) {
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


    MarkerOptions markerOptions;
    public static MarkerOptions displayMarker(Activity act, LatLng location, boolean isOrigin) {
        // make current location marker
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new com.google.android.gms.maps.model.LatLng(location.lat, location.lng));

        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marker_view = inflater.inflate(R.layout.maps_marker, null);
        ImageView marker = (ImageView) marker_view.findViewById(R.id.marker);
        marker.setBackgroundResource(isOrigin ? R.drawable.marker_origin : R.drawable.marker_destination);
        if (isOrigin) {
            marker.setImageResource(R.drawable.ic_origin);
        }

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(act, marker_view)));
        return markerOptions;
        //mMap.addMarker(markerOptions);
    }



    // -------------------------  REMOVER ------------------- REMOVER .....................
    private static GoogleApiClient mGoogleApiClient;
    public  static LatLng getLatLng (Context ctx) {

        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
               // .enableAutoManage(, 1, null)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();

        LatLng qLocation = new LatLng(0,0);
        if (mGoogleApiClient.isConnected()) {
            //https://stackoverflow.com/questions/34018781/how-to-get-country-specific-results-in-google-places-api-for-autocompletepredict/39785267
            com.google.android.gms.common.api.PendingResult<PlaceBuffer> result;
            result = Places.GeoDataApi.getPlaceById(mGoogleApiClient, "ChIJrTLr-GyuEmsRBfy61i59si0");
            PlaceBuffer placeBuffer = result.await(60, TimeUnit.SECONDS);
            final Status status = placeBuffer.getStatus();
            if (!status.isSuccess()) {
                placeBuffer.release();
                Toast.makeText(ctx, "Error : " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            // dados aqui ...
            final Place mPlace = placeBuffer.get(0);
            double qLal = mPlace.getLatLng().latitude;
            double qLng = mPlace.getLatLng().longitude;
            qLocation = new LatLng(qLal,qLng);

            return qLocation;
        }
    return qLocation;
    }






}
