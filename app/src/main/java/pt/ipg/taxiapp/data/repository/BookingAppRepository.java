package pt.ipg.taxiapp.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.data.persistance.dao.BookingDao;
import pt.ipg.taxiapp.data.persistance.local.AppDatabase;

public class BookingAppRepository {
    private BookingDao bookingDao;
    private LiveData<List<Booking>> allBookings;
    private LiveData<List<Booking>> allActiveBookings;
    private LiveData<List<Booking>> allFinishedBookings;

    public BookingAppRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        bookingDao = database.bookingDao();
        allBookings = bookingDao.getAllBookings();
        allActiveBookings = bookingDao.getAllActiveBookings();
        allFinishedBookings = bookingDao.getAllFinishedBookings();

    }

    public void insert(Booking booking){
        new BookingAppRepository.InsertBookingAsyncTask(bookingDao).execute(booking);
    }

    public void update(Booking booking){
        new BookingAppRepository.UpdateBookingAsyncTask(bookingDao).execute(booking);

    }

    public void delete(Booking booking){
        new BookingAppRepository.DeleteBookingAsyncTask(bookingDao).execute(booking);

    }

    public void deleteAllBookings(){
        new BookingAppRepository.DeleteAllBookingAsyncTask(bookingDao).execute();

    }

    public LiveData<List<Booking>> getAllBookings() {
        return allBookings;
    }


    public LiveData<List<Booking>> getAllActiveBookings() {return allActiveBookings;
    }

    public LiveData<List<Booking>> getAllFinishedBookings() {return allFinishedBookings;
    }

// ------------------------------------------------------------------------

    private static class InsertBookingAsyncTask extends AsyncTask<Booking, Void, Long> {
        private BookingDao bookingDao;
        private InsertBookingAsyncTask(BookingDao bookingDao){
            this.bookingDao = bookingDao;
        }

        @Override
        protected Long doInBackground(Booking... bookings) {
            long ID = bookingDao.insert(bookings[0]);
            return ID;
        }



    }


    private static class UpdateBookingAsyncTask extends AsyncTask<Booking, Void, Void>{
        private BookingDao bookingDao;
        private UpdateBookingAsyncTask(BookingDao bookingDao){
            this.bookingDao = bookingDao;
        }

        @Override
        protected Void doInBackground(Booking... bookings) {
            bookingDao.update(bookings[0]);
            return null;
        }
    }


    private static class DeleteBookingAsyncTask extends AsyncTask<Booking, Void, Void>{
        private BookingDao bookingDao;
        private DeleteBookingAsyncTask(BookingDao bookingDao){
            this.bookingDao = bookingDao;
        }

        @Override
        protected Void doInBackground(Booking... bookings) {
            bookingDao.delete(bookings[0]);
            return null;
        }
    }


    private static class DeleteAllBookingAsyncTask extends AsyncTask<Void, Void, Void>{
        private BookingDao bookingDao;
        private DeleteAllBookingAsyncTask(BookingDao bookingDao){
            this.bookingDao = bookingDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bookingDao.deleteAllBookings();
            return null;
        }
    }



}


