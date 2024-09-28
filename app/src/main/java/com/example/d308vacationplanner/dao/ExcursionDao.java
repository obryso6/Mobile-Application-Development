
package com.example.d308vacationplanner.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308vacationplanner.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursion_table WHERE vacationID = :vacationId")
    LiveData<List<Excursion>> getExcursionsForVacation(int vacationId);

    @Query("SELECT * FROM excursion_table WHERE excursionID = :excursionId")
    Excursion getExcursionById(int excursionId);

    @Query("SELECT COUNT(*) FROM excursion_table WHERE vacationID = :vacationId")
    int countExcursionsForVacation(int vacationId);

}
