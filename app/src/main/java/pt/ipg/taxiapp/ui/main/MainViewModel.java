package pt.ipg.taxiapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.LatLng;

import java.util.List;

import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.data.model.User;
import pt.ipg.taxiapp.data.repository.BookingAppRepository;
import pt.ipg.taxiapp.data.repository.UserAppRepository;


public class MainViewModel extends AndroidViewModel {
    private UserAppRepository repository;

    private LiveData<List<User>> allUsers;



    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new UserAppRepository(application);
        allUsers = repository.getAllUsers();

    }


    public LiveData<List<User>> getAllUserss() {
        return allUsers;
    }


}
