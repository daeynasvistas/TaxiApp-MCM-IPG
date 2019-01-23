package pt.ipg.taxiapp.data;

import android.content.Context;
import android.content.res.TypedArray;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.data.model.RideClass;
import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.model.TaxiPosition;

public class Constant {

    private static Random rnd = new Random();

    private static int getRandomIndex(Random r, int min, int max) {
        return r.nextInt(max - min) + min;
    }

    public static List<TaxiPosition> getTaxiAroundData(Context ctx) {
        List<TaxiPosition> items = new ArrayList<>();
        String[] locations = ctx.getResources().getStringArray(R.array.car_location);
        for (int i = 0; i < locations.length; i++) {
            TaxiPosition item = new TaxiPosition();
            String[] loc = locations[i].split("#");
            item.latLng = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
            item.rotation = getRandomIndex(rnd, 0, 360);
            items.add(item);
        }
        return items;
    }

    public static List<TaxiPosition> getTaxiArounddb(List<Taxi> taxi) {
        List<TaxiPosition> pins = new ArrayList<>();

        for (Taxi t : taxi) {
            TaxiPosition pin = new TaxiPosition();
            pin.latLng = new LatLng(t.getLat(), t.getLng());
            pin.rotation = getRandomIndex(rnd, 0, 360);
            pins.add(pin);
         }

        return pins;
    }


// alterar para vers 0.8 .. sync com API para atualizar os preços
    public static List<RideClass> getRideClassData(Context ctx) {
        List<RideClass> items = new ArrayList<>();
        TypedArray images = ctx.getResources().obtainTypedArray(R.array.ride_image);

        String[] names = ctx.getResources().getStringArray(R.array.ride_name);
        String[] prices = ctx.getResources().getStringArray(R.array.ride_price);
        String[] paxs = ctx.getResources().getStringArray(R.array.ride_pax);
        String[] durations = ctx.getResources().getStringArray(R.array.ride_duration);

        for (int i = 0; i < names.length; i++) {
            RideClass item = new RideClass();
            item.class_name = names[i];
            item.image = images.getResourceId(i, -1);
            item.price = prices[i];
            item.pax = paxs[i] + " pess";
            item.duration = durations[i] + " min";
            items.add(item);
        }
        return items;
    }
}
