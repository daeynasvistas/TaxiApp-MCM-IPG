package pt.ipg.taxiapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.repository.TaxiAppRepository;

public class TaxiViewModel extends AndroidViewModel {
    private TaxiAppRepository repository;
    private LiveData<List<Taxi>> allTaxis;


    public TaxiViewModel(@NonNull Application application) {
        super(application);

        repository = new TaxiAppRepository(application);
        allTaxis = repository.getAllTaxis();
    }

    public void insert(Taxi taxi) {
        repository.insert(taxi);
    }

    public void update(Taxi taxi) {
        repository.update(taxi);
    }

    public void delete(Taxi taxi) {
        repository.delete(taxi);
    }

    public void deleteAllTaxis() {
        repository.deleteAllTaxis();
    }

    public LiveData<List<Taxi>> getAllTaxis() {
        return allTaxis;
    }

}
