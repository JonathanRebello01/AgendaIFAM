package com.example.agendaifam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaifam.R;
import com.example.agendaifam.models.mReserva;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReservasCalendarAdapter extends RecyclerView.Adapter<ReservasCalendarAdapter.ReservaViewHolder> {
    private List<mReserva> reservas;

    public ReservasCalendarAdapter(List<mReserva> reservas) {
        this.reservas = reservas;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservas_calendar, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        mReserva r = reservas.get(position);
        holder.local.setText("LOCAL: " + r.getNomeEspaco());
        holder.nomeSolicitante.setText("Solicitante: " + r.getNomeProfessorReserva());
        holder.dataReserva.setText("Solicitante: " + formatDateBr(r.getDataReserva()));
        holder.horaInicioReserva.setText("Solicitante: " + formatTimeBr(r.getHoraInicioReserva()));
        holder.horaFimReserva.setText("Solicitante: " + formatTimeBr(r.getHoraFimReserva()));
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }


    public static String formatDateBr(Timestamp timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(timestamp.toDate());
    }


    public static String formatTimeBr(Timestamp timestamp) {
        if (timestamp == null) return "";
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(timestamp.toDate());
    }


    public void updateData(List<mReserva> newReservas) {
        this.reservas.clear();
        this.reservas.addAll(newReservas);
        notifyDataSetChanged();
    }
    static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView local, nomeSolicitante, dataReserva, horaInicioReserva, horaFimReserva;
        ReservaViewHolder(View itemView) {
            super(itemView);
            local = itemView.findViewById(R.id.tvLocal);
            nomeSolicitante = itemView.findViewById(R.id.tvNomeSolicitante);
            dataReserva = itemView.findViewById(R.id.tvDataReserva);
            horaInicioReserva = itemView.findViewById(R.id.tvHoraInicio);
            horaFimReserva = itemView.findViewById(R.id.tvHoraFim);
        }
    }
}