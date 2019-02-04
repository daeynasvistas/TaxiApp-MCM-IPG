package pt.ipg.taxiapp.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.data.persistance.dao.TaxiDao;
import pt.ipg.taxiapp.data.persistance.local.AppDatabase;

public class TaxiAppRepository {
    private TaxiDao taxiDao;
    private LiveData<List<Taxi>> allTaxis;

    public TaxiAppRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        taxiDao = database.taxiDao();
        allTaxis = taxiDao.getAllTaxis();

    }

    public void insert(Taxi taxi){

        new InsertTaxiAsyncTask(taxiDao).execute(taxi);
    }

    public void update(Taxi taxi){
        new UpdateTaxiAsyncTask(taxiDao).execute(taxi);

    }

    public void delete(Taxi taxi){
        new DeleteTaxiAsyncTask(taxiDao).execute(taxi);

    }

    public void deleteAllTaxis(){
        new DeleteAllTaxiAsyncTask(taxiDao).execute();

    }


    public LiveData<List<Taxi>> getAllTaxis() {
        return allTaxis;
    }



// ------------------------------------------------------------------------

    private static class InsertTaxiAsyncTask extends AsyncTask<Taxi, Void, Void>{
        private TaxiDao taxiDao;
        private InsertTaxiAsyncTask(TaxiDao taxiDao){
            this.taxiDao = taxiDao;
        }

        @Override
        protected Void doInBackground(Taxi... taxis) {
            taxiDao.insert(taxis[0]);
            return null;
        }
    }


    private static class UpdateTaxiAsyncTask extends AsyncTask<Taxi, Void, Void>{
        private TaxiDao taxiDao;
        private UpdateTaxiAsyncTask(TaxiDao taxiDao){
            this.taxiDao = taxiDao;
        }

        @Override
        protected Void doInBackground(Taxi... taxis) {
            taxiDao.update(taxis[0]);
            return null;
        }
    }


    private static class DeleteTaxiAsyncTask extends AsyncTask<Taxi, Void, Void>{
        private TaxiDao taxiDao;
        private DeleteTaxiAsyncTask(TaxiDao taxiDao){
            this.taxiDao = taxiDao;
        }

        @Override
        protected Void doInBackground(Taxi... taxis) {
            taxiDao.delete(taxis[0]);
            return null;
        }
    }


    private static class DeleteAllTaxiAsyncTask extends AsyncTask<Void, Void, Void>{
        private TaxiDao taxiDao;
        private DeleteAllTaxiAsyncTask(TaxiDao taxiDao){
            this.taxiDao = taxiDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taxiDao.deleteAllTaxis();
            return null;
        }
    }



}
