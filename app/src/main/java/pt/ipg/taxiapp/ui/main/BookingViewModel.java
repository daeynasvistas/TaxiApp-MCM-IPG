package pt.ipg.taxiapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.data.repository.BookingAppRepository;


public class BookingViewModel extends AndroidViewModel {

    private BookingAppRepository repository;
    private LiveData<List<Booking>> allBookings;
    private LiveData<List<Booking>> allActiveBookings;
    private LiveData<List<Booking>> allFinishedBookings;

    public BookingViewModel(@NonNull Application application) {
        super(application);

        repository = new BookingAppRepository(application);
        allBookings = repository.getAllBookings();
        allActiveBookings = repository.getAllActiveBookings();
        allFinishedBookings = repository.getAllFinishedBookings();
    }

    public void insert(Booking booking) {// long para receber o Id inserido!!!
        repository.insert(booking);
     }

    public void update(Booking booking) { repository.update(booking);
    }

    public void delete(Booking booking) {
        repository.delete(booking);
    }

    public void deleteAllBookings() {
        repository.deleteAllBookings();
    }

    public LiveData<List<Booking>> getAllBookings() {
        return allBookings;
    }
    // todos os ativos
    public LiveData<List<Booking>> getAllActiveBookings() {
        return allActiveBookings;
    }
    // Todos menos ativos
    public LiveData<List<Booking>> getAllFinishedBookings() { return allFinishedBookings; }
}
