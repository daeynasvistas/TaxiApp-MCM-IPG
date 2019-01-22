package pt.ipg.taxiapp.data.persistance.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


import pt.ipg.taxiapp.data.model.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM USER_TABLE")
    void deleteAllUsers();

    @Query("SELECT * FROM USER_TABLE")
    LiveData<List<User>> getAllUsers();


}
