package com.example.d308vacationplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;

import com.example.d308vacationplanner.UI.VacationDetailsView;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private final List<Vacation> vacationList;
    private final LayoutInflater inflater;

    public VacationAdapter(Context context, List<Vacation> vacationList) {
        this.vacationList = vacationList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_vacation, parent, false);
        return new VacationViewHolder(itemView, vacationList);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation currentVacation = vacationList.get(position);
        holder.vacationNameTextView.setText(currentVacation.getVacationName());
        holder.hotelNameTextView.setText(currentVacation.getHotel());
    }

    @Override
    public int getItemCount() {
        return vacationList.size();
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacationList.clear();
        this.vacationList.addAll(vacations);
        notifyDataSetChanged();
    }

    public static class VacationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vacationNameTextView;
        TextView hotelNameTextView;
        private final List<Vacation> vacationList;

        public VacationViewHolder(View itemView, List<Vacation> vacationList) {
            super(itemView);
            this.vacationList = vacationList;

            vacationNameTextView = itemView.findViewById(R.id.textView_vacation_name);
            hotelNameTextView = itemView.findViewById(R.id.textView_hotel_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Vacation clickedVacation = vacationList.get(position);
                Intent intent = new Intent(view.getContext(), VacationDetailsView.class);
                intent.putExtra("vacationId", clickedVacation.getVacationID());
                view.getContext().startActivity(intent);
            }
        }
    }
}

