package pt.ipg.taxiapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.data.Constant;
import pt.ipg.taxiapp.data.model.TaxiPosition;

public class Tools {


    public static void systemBarLolipop(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }

    public static void setSystemBarLight(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = act.findViewById(android.R.id.content);
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }


    public static void setCompleteSystemBarLight(Activity act) {
        Tools.setSystemBarColor(act, R.color.grey_soft);
        Tools.setSystemBarLight(act);
    }
    public static void setSystemBarColorFragment(Activity act, DialogFragment dialogFragment, @ColorRes int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialogFragment.getDialog().getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(act.getResources().getColor(color));
            }
        } catch (Exception e) {
        }
    }

    public static void setSystemBarLightFragment(DialogFragment dialogFragment) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View view = dialogFragment.getDialog().getWindow().getDecorView();
                int flags = view.getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                view.setSystemUiVisibility(flags);
            }
        } catch (Exception e) {
        }
    }



    //-------------------------- Vers 0.2 ---------------------------

    // ------------------------- GOOGLE MAPAS .------------------------
    // https://stackoverflow.com/questions/50259675/clickable-google-maps-marker-in-fragment?rq=1

    public static GoogleMap configBasicGoogleMap(GoogleMap googleMap) {
        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Enable / Disable zooming controls
        // googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(false);
        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        // enable traffic layer
        googleMap.setTrafficEnabled(false);

        return googleMap;
    }
    // ------------------------- GOOGLE MAPAS FIM -----------------------



    private static boolean isConnect(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected() || activeNetworkInfo.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    public static void showToastMiddle(Context ctx, String message) {
        Toast toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void checkInternetConnection(Context ctx) {
        if (!isConnect(ctx)) {
            showToastMiddle(ctx, "Não tens internet Amigo!!");
        }
    }

    // ver aqui http://androidtechpoint.blogspot.com/2017/08/converting-android-view-to-bitmap.html
    public static Bitmap createBitmapFromView(Activity act, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }




    public static void displayCarAroundMarkers(Activity act, GoogleMap googleMap) {
        List<TaxiPosition> items = Constant.getTaxiAroundData(act);
        for (TaxiPosition c : items) {
            displayMarker(act, googleMap, c);
        }
    }


    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
        marker.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(act, 40), dpToPx(act, 40)));
        marker.setRotation(c.rotation);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(act, marker_view)));
        googleMap.addMarker(markerOptions);
    }






    // retirado daqui, só para criar posições random para vers 0.4
    // https://stackoverflow.com/questions/33976732/generate-random-latlng-given-device-location-and-radius
    public static LatLng getRandomLocation(LatLng point, int radius) {

        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for(int i = 0; i<10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));
        }
        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);
    }


    public static String getToken() {

        return null;
    }
}

