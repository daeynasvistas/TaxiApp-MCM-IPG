package pt.ipg.taxiapp.data.persistance.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.Update;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pt.ipg.taxiapp.data.model.Taxi;

@Dao
public interface TaxiDao {

    @Insert
    void insert(Taxi taxi);

    @Update
    void update(Taxi taxi);

    @Delete
    void delete(Taxi taxi);

    @Query("DELETE FROM TAXI_TABLE")
    void deleteAllTaxis();

    @Query("SELECT * FROM TAXI_TABLE")
    LiveData<List<Taxi>> getAllTaxis();


}
