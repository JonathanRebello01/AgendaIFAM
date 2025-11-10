package com.example.agendaifam.fragments;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.agendaifam.R;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final List<EventDay> diasComEventos = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CalendarView calendarView;
    private LinearLayout eventsContainer;

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


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        eventsContainer = view.findViewById(R.id.eventsContainer);
        db = FirebaseFirestore.getInstance();
        ((com.applandeo.materialcalendarview.CalendarView) calendarView)
                .setOnDayClickListener(eventDay -> {

                    Calendar clicked = eventDay.getCalendar();

                    int year = clicked.get(Calendar.YEAR);
                    int month = clicked.get(Calendar.MONTH);
                    int day = clicked.get(Calendar.DAY_OF_MONTH);

                    String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month+1, day);

                    eventsContainer.removeAllViews();
                    adicionarTexto("Carregando reservas...");

                    loadReservationsForDate(year, month, day, dateStr);
                });

//        // Atualiza título do mês ao iniciar
//        updateMonthTitle(calendarView.getDate());
//
//        // Scroll para hoje (opcional)
//        calendarView.setDate(System.currentTimeMillis(), false, true);

//        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
//
//            // month no CalendarView começa em 0 → por isso usamos month diretamente
//            Calendar c = Calendar.getInstance();
//            c.set(year, month, dayOfMonth, 0, 0, 0);
//
//            // converte para yyyy-MM-dd
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            String dateStr = sdf.format(c.getTime());
//
//            // Limpa lista antes de carregar novos itens
//            eventsContainer.removeAllViews();
//            adicionarTexto("Carregando reservas...");
//
//            // 🔥 Chama Firestore
//            loadReservationsForDate(year, month, dayOfMonth, dateStr);
//
//        });

            // Atualiza título do mês se o usuário navegar (API nativa não tem callback pra mês trocado,
        // então registramos um listener simples no onScrollChange — em alguns dispositivos pode não disparar.
        // Aqui deixamos o título atualizado quando a data muda (suficiente para seleções).
        carregarDiasComEventos();

        return view;
    }

    private void adicionarTexto(String texto) {
        TextView tv = new TextView(requireContext());
        tv.setText(texto);
        tv.setTextSize(16f);
        tv.setPadding(8, 8, 8, 8);
        eventsContainer.addView(tv);
    }

    private void updateMonthTitle(long millis) {
        Date d = new Date(millis);
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    }

    private void loadReservationsForDate(int year, int monthZeroBased, int dayOfMonth, String selectedDateStr) {
        final List<String> results = new ArrayList<>();

        db.collection("reservas")
                .whereEqualTo("dataReserva", selectedDateStr)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            String hi = safeGetString(doc, "horaInicioReserva");
                            String hf = safeGetString(doc, "horaFimReserva");
                            String who = safeGetString(doc, "solicitante"); // opcional
                            results.add(formatReservaLine(hi, hf, who));
                        }
                        // Atualiza a lista no Scroll
                        eventsContainer.removeAllViews();

                        if (results.isEmpty()) {
                            adicionarTexto("Nenhuma reserva para este dia.");
                        } else {
                            for (String r : results) {
                                adicionarTexto("• " + r);
                            }
                        }
                    } else {
                        // Se não encontrou por string, tenta buscar por Timestamp/Dates no intervalo do dia.
                        queryReservationsByTimestampRange(year, monthZeroBased, dayOfMonth);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("CalendarFragment", "Erro ao buscar reservas (by string)", e);
                    // fallback: também tentar por timestamp
                    queryReservationsByTimestampRange(year, monthZeroBased, dayOfMonth);
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