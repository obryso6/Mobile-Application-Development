package com.example.d308vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.adapters.VacationAdapter;
import com.example.d308vacationplanner.database.VacationRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VacationList extends AppCompatActivity {

    private VacationAdapter vacationAdapter;
    private static final int REQUEST_CODE_ADD_VACATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        // Initialize the RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView_vacations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vacationAdapter = new VacationAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(vacationAdapter);

        VacationRepository vacationRepository = new VacationRepository(getApplication());

        // Observe the vacation data
        vacationRepository.getAllVacations().observe(this, vacations -> {
            if (vacations != null) {
                vacationAdapter.setVacations(vacations);
            }
        });

        // Set up the FAB to navigate to the VacationDetails activity (which acts as the form)
        FloatingActionButton fab = findViewById(R.id.fab_add_vacation);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_VACATION);
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_excursion) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_VACATION && resultCode == RESULT_OK) {
            // Refresh the vacation list after a new vacation is added
            VacationRepository vacationRepository = new VacationRepository(getApplication());
            vacationRepository.getAllVacations().observe(this, vacations -> {
                if (vacations != null) {
                    vacationAdapter.setVacations(vacations);
                }
            });
        }
    }
}
