package pt.ipg.taxiapp.data.persistance.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pt.ipg.taxiapp.data.model.Booking;


@Dao
public interface BookingDao {
    @Insert
    void insert(Booking booking);

    @Update
    void update(Booking booking);

    @Delete
    void delete(Booking booking);

    @Query("DELETE FROM BOOKING_TABLE")
    void deleteAllBookings();

    @Query("SELECT * FROM BOOKING_TABLE")
    LiveData<List<Booking>> getAllBookings();

}
