package pt.ipg.taxiapp.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;


import pt.ipg.taxiapp.data.model.User;

import pt.ipg.taxiapp.data.persistance.dao.UserDao;
import pt.ipg.taxiapp.data.persistance.local.AppDatabase;

public class UserAppRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    public UserAppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getAllUsers();

    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    //--------------------------------  Apenas os necessário .....
    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private InsertUserAsyncTask(UserDao userDao){
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }






}
