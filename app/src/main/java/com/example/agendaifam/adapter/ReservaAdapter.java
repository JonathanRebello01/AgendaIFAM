package com.example.agendaifam.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaifam.R;
import com.example.agendaifam.models.mEspacos;
import com.example.agendaifam.models.mReserva;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class ReservaAdapter extends RecyclerView.Adapter {

    private List<mEspacos> espacoList;
    private Context ctx;



    public ReservaAdapter(List<mEspacos> espacoList, Context ctx) {
        this.espacoList = espacoList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.item_reserva, parent, false);

        return new ReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mEspacos espaco = espacoList.get(position);
        String nome = espaco.getNome();
        String descricao = espaco.getDescricao();
        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ReservaViewHolder reservaViewHolder = (ReservaViewHolder) holder;
        reservaViewHolder.nome_reservar.setText(nome);
        reservaViewHolder.descricao_reservar.setText(descricao);

        mReserva reservaAtual = new mReserva(null, null, null, espaco.getId(), usuarioID, null, null, null, null, espaco.getNome(), espaco.getDescricao(), 0, espaco.getCod_departamento());

        reservaViewHolder.selecionar_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        (view, selectedYear, selectedMonth, selectedDay) -> {

                            Calendar selectedCalendar = Calendar.getInstance();
                            selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

                            Date selectedDate = selectedCalendar.getTime();
                            Timestamp firebaseTimestamp = new Timestamp(selectedDate);

                            reservaAtual.setDataReserva(firebaseTimestamp);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String formattedDate = dateFormat.format(selectedDate);

                            Toast.makeText(context, "Data selecionada: " + formattedDate, Toast.LENGTH_SHORT).show();

                            reservaViewHolder.selecionar_data.setText(formattedDate);
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        reservaViewHolder.selecionar_horario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        (view, selectedHour, selectedMinute) -> {
                            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);

                            Toast.makeText(context, "Horário selecionado: " + formattedTime, Toast.LENGTH_SHORT).show();

                            reservaViewHolder.selecionar_horario.setText(formattedTime);

                            Calendar selectedCalendar = Calendar.getInstance();
                            selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                            selectedCalendar.set(Calendar.MINUTE, selectedMinute);


                            Timestamp firebaseTimestampHora = new Timestamp(selectedCalendar.getTime());

                            reservaAtual.setHoraInicioReserva(firebaseTimestampHora);
                        },
                        hour, minute, true);
                timePickerDialog.show();
            }
        });

        reservaViewHolder.btn_reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("reservas");
                String documentId = collectionReference.document().getId();
                reservaAtual.setIdReserva(documentId);
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("ContaPrefs", Context.MODE_PRIVATE);
                String nome = sharedPreferences.getString("nome", null);
                reservaAtual.setNomeProfessorReserva(nome);

                collectionReference.document(documentId).set(reservaAtual).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(v.getContext(), "Reserva realizada com sucesso!", Toast.LENGTH_SHORT).show();
                }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Erro ao realizar a reserva!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    @Override
    public int getItemCount() {
        return espacoList.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView descricao_reservar, selecionar_data, selecionar_horario, nome_reservar;
        Button btn_reservar;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_reservar = itemView.findViewById(R.id.texto_titulo_reserva);
            descricao_reservar = itemView.findViewById(R.id.texto_descricao_reserva);
            selecionar_horario = itemView.findViewById(R.id.selecionar_horario);
            btn_reservar = itemView.findViewById(R.id.btn_reservar);
            selecionar_data = itemView.findViewById(R.id.selecionar_data);

        }

    }

}
