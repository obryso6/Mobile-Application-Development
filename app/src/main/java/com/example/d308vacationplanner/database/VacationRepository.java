package com.example.d308vacationplanner.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.d308vacationplanner.dao.ExcursionDao;
import com.example.d308vacationplanner.dao.VacationDao;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationRepository {
    private final VacationDao vacationDao;
    private final ExcursionDao excursionDao;
    private final ExecutorService databaseExecutor;

    private final LiveData<List<Vacation>> allVacations;

    public VacationRepository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        vacationDao = db.vacationDao();
        excursionDao = db.excursionDao();
        databaseExecutor = Executors.newFixedThreadPool(4);

        allVacations = vacationDao.getAllVacations();
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }

    public Vacation getVacationById(int vacationId) {
        return vacationDao.getVacationById(vacationId);
    }

    public void insertVacation(Vacation vacation) {
        databaseExecutor.execute(() -> vacationDao.insertVacation(vacation));
    }

    public void updateVacation(Vacation vacation) {
        databaseExecutor.execute(() -> {
            Log.d("VacationRepository", "Updating vacation: " + vacation.getVacationName());
            vacationDao.update(vacation);
        });
    }

    public void deleteVacation(Vacation vacation, DeleteVacationCallback callback) {
        new Thread(() -> {
            try {
                // Check if there are any excursions associated with the vacation
                int excursionCount = excursionDao.countExcursionsForVacation(vacation.getVacationID());
                if (excursionCount > 0) {
                    // If there are excursions, notify the callback with a failure message
                    callback.onFailure("Cannot delete vacation. There are associated excursions.");
                } else {
                    // If no excursions, perform the deletion
                    vacationDao.deleteVacation(vacation);
                    // Notify the success callback
                    callback.onSuccess();
                }
            } catch (Exception e) {
                // Notify the failure callback with an error message
                callback.onFailure("Failed to delete vacation: " + e.getMessage());
            }
        }).start();
    }

    public LiveData<List<Excursion>> getExcursionsForVacation(int vacationId) {
        return excursionDao.getExcursionsForVacation(vacationId);
    }

    public void insertExcursion(Excursion excursion) {
        databaseExecutor.execute(() -> excursionDao.insert(excursion));
    }

    public void updateExcursion(Excursion excursion) {
        databaseExecutor.execute(() -> excursionDao.update(excursion));
    }

    public void deleteExcursion(Excursion excursion) {
        databaseExecutor.execute(() -> excursionDao.delete(excursion));
    }

    public Excursion getExcursionById(int excursionId) {
        return excursionDao.getExcursionById(excursionId);
    }

    public interface DeleteVacationCallback {
        void onSuccess();

        void onExcursionEdit(Excursion excursion);

        void onFailure(String message);
    }
}
