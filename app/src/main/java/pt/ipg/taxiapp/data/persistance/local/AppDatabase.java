package pt.ipg.taxiapp.data.persistance.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.maps.model.LatLng;


import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.model.User;
import pt.ipg.taxiapp.data.persistance.dao.TaxiDao;
import pt.ipg.taxiapp.data.persistance.dao.UserDao;
import pt.ipg.taxiapp.utils.Tools;

// vers 0.5 com tax e user como tabelas
@Database(entities = {Taxi.class, User.class},version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract TaxiDao taxiDao(); // taxis
    public abstract UserDao userDao(); // utilizador (talvez vário sno futuro Vers 1.1)

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "taxi_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack) // Populate database seeding
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{ // <-----------Seeding

        private TaxiDao taxiDao;
        private PopulateDbAsyncTask(AppDatabase db){
            taxiDao = db.taxiDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LatLng point = new LatLng(40.777570, -7.349922);

            // Receber lista de TAXi da API  -- todo vers 0.7
            for(int i=0;i<50;i++){
                LatLng randPin = Tools.getRandomLocation(point,2500);
                taxiDao.insert(new Taxi("Daniel Mendes","Daniel@ept.pt","foto(alterar)",0,randPin.lat,randPin.lng));
            }

            return null;
        }



    }
}
