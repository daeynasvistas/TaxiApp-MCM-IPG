package pt.ipg.taxiapp.data.persistance.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;


import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.persistance.dao.TaxiDao;
import pt.ipg.taxiapp.utils.Tools;


@Database(entities = {Taxi.class},version = 1, exportSchema = false)
public abstract class TaxiAppDatabase extends RoomDatabase {
    private static TaxiAppDatabase instance;
    public abstract TaxiDao taxiDao();

    public static synchronized TaxiAppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaxiAppDatabase.class, "taxi_database")
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
        private PopulateDbAsyncTask(TaxiAppDatabase db){
            taxiDao = db.taxiDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LatLng point = new LatLng(40.777570, -7.349922);
            for(int i=0;i<50;i++){
                LatLng randPin = Tools.getRandomLocation(point,2500);
                taxiDao.insert(new Taxi("Daniel Mendes","Daniel@ept.pt","foto(alterar)",0,randPin.latitude,randPin.longitude));
            }
            return null;
        }



    }
}
