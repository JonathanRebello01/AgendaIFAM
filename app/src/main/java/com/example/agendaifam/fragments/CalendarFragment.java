package com.example.agendaifam.fragments;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.agendaifam.R;
import com.example.agendaifam.adapter.ReservasCalendarAdapter;
import com.example.agendaifam.models.mReserva;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    private final List<EventDay> diasComEventos = new ArrayList<>();
    private CalendarView calendarView;
    ReservasCalendarAdapter adapter = new ReservasCalendarAdapter(new ArrayList<>());
    private FirebaseFirestore db;

    public CalendarFragment() {
        // Required empty public constructor
    }
    private void carregarDiasComEventos() {
        db.collection("reservas")
                .get()
                .addOnSuccessListener(query -> {
                    diasComEventos.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        // Tenta pegar como Timestamp (caso salvo dessa forma)
                        Object dataObj = doc.get("dataReserva");
                        Calendar c = Calendar.getInstance();
                        if (dataObj instanceof com.google.firebase.Timestamp) {
                            Date date = ((com.google.firebase.Timestamp) dataObj).toDate();
                            c.setTime(date);
                        } else if (dataObj instanceof Date) {
                            // Caso venha direto como Date
                            c.setTime((Date) dataObj);
                        } else if (dataObj instanceof String) {
                            // Caso venha como "yyyy-MM-dd"
                            try {
                                String dataStr = (String) dataObj;
                                String[] p = dataStr.split("-");
                                int y = Integer.parseInt(p[0]);
                                int m = Integer.parseInt(p[1]) - 1;
                                int d = Integer.parseInt(p[2]);
                                c.set(y, m, d);
                            } catch (Exception e) {
                                Log.e("CalendarFragment", "Erro ao converter dataReserva String", e);
                                continue;
                            }
                        } else {
                            // Formato inesperado → ignora
                            Log.w("CalendarFragment", "Formato desconhecido de dataReserva: " + dataObj);
                            continue;
                        }

                        // Adiciona evento com ícone
                        diasComEventos.add(new EventDay(c, R.drawable.ic_event_marker));
                    }

                    // Aplica eventos no calendário
                    calendarView.setEvents(diasComEventos);
                });
    }

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        db = FirebaseFirestore.getInstance();

        RecyclerView reservasRecyclerView = view.findViewById(R.id.reservasRecyclerView);
        reservasRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        reservasRecyclerView.setAdapter(adapter);

        carregarDiasComEventos();

        (calendarView)
                .setOnDayClickListener(eventDay -> {

                    Calendar clicked = eventDay.getCalendar();

                    try {
                        calendarView.setDate(clicked);
                    } catch (OutOfDateRangeException e) {
                        throw new RuntimeException(e);
                    }


                    int year = clicked.get(Calendar.YEAR);
                    int month = clicked.get(Calendar.MONTH);
                    int day = clicked.get(Calendar.DAY_OF_MONTH);

                    String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month+1, day);

                    loadReservationsForDate(year, month, day, dateStr);
                });




        return view;
    }


    private void loadReservationsForDate(int year, int monthZeroBased, int dayOfMonth, String selectedDateStr) {
        db.collection("reservas")
                .whereEqualTo("dataReserva", selectedDateStr)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        List<mReserva> reservas = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : querySnapshot) {

                            Timestamp hi = doc.getTimestamp("horaInicioReserva");
                            Timestamp hf = doc.getTimestamp("horaFimReserva");
                            Timestamp dataReserva = doc.getTimestamp("dataReserva");

                            String professor = safeGetString(doc, "nomeProfessorReserva");
                            String nomeEspaco = safeGetString(doc, "nomeEspaco");
                            reservas.add(new mReserva(nomeEspaco, professor, hi, hf, dataReserva));
                        }

                            adapter.updateData(reservas);

                    }

                });
    }



        private void queryReservationsByTimestampRange(int year, int monthZeroBased, int dayOfMonth) {
        Calendar start = Calendar.getInstance();
        start.set(year, monthZeroBased, dayOfMonth, 0, 0, 0);
        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.set(year, monthZeroBased, dayOfMonth, 23, 59, 59);
        end.set(Calendar.MILLISECOND, 999);

        Date startDate = start.getTime();
        Date endDate = end.getTime();

        db.collection("reservas")
                .whereGreaterThanOrEqualTo("dataReserva", startDate)
                .whereLessThanOrEqualTo("dataReserva", endDate)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> results = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        // dataReserva pode ser Timestamp ou Date; não precisamos do valor aqui
                        String hi = safeGetString(doc, "horaInicioReserva");
                        String hf = safeGetString(doc, "horaFimReserva");
                        String who = safeGetString(doc, "solicitante");
                        results.add(formatReservaLine(hi, hf, who));

                    }
                    String dateLabel = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(startDate);
                    showReservasDialog(dateLabel, results);
                })
                .addOnFailureListener(e -> {
                    Log.e("CalendarFragment", "Erro ao buscar reservas (by timestamp)", e);
                    // mostra mensagem vazia
                    showReservasDialog(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startDate), new ArrayList<>());
                });
    }
    private String formatReservaLine(String horaInicio, String horaFim, String solicitante) {
        if (horaInicio == null || horaFim == null) return "Reserva sem horário";

        return horaInicio + " - " + horaFim + (solicitante != null ? " (" + solicitante + ")" : "");
    }

    private String safeGetString(QueryDocumentSnapshot doc, String field) {
        Object o = doc.get(field);
        if (o == null) return null;
        return o.toString();
    }

    private void showReservasDialog(String dateLabel, List<String> linhas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Reservas - " + dateLabel);

        if (linhas == null || linhas.isEmpty()) {
            builder.setMessage("Nenhuma reserva neste dia.");
            builder.setPositiveButton("OK", null);
            builder.show();
            return;
        }

        // Monta mensagem com cada linha em nova linha
        StringBuilder msg = new StringBuilder();
        for (String l : linhas) {
            msg.append("• ").append(l).append("\n");
        }

        builder.setMessage(msg.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}