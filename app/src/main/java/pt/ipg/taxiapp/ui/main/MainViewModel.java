package pt.ipg.taxiapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.model.User;
import pt.ipg.taxiapp.data.repository.TaxiAppRepository;
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
