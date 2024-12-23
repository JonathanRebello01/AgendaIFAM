package com.example.agendaifam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaifam.R;
import com.example.agendaifam.models.mReserva;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SolicitacaoProfAdapter extends RecyclerView.Adapter{

    private List<mReserva> reservaProfList;

    public SolicitacaoProfAdapter(List<mReserva> reservaList) {
        if (reservaList == null) {
            this.reservaProfList = new ArrayList<>();
        } else {
            this.reservaProfList = reservaList;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.solicitacaoprof_item, parent, false);

        return new SolicitacaoProfAdapter.SolicitacaoProfViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String nome = reservaProfList.get(position).getNomeEspaco();
        String descricao = reservaProfList.get(position).getDescricaoEspaco();
        Timestamp data = reservaProfList.get(position).getDataReserva();
        Timestamp hora = reservaProfList.get(position).getHoraInicioReserva();
        int status = reservaProfList.get(position).getStatusReserva();


        SolicitacaoProfAdapter.SolicitacaoProfViewHolder solicitacaoProfViewHolder = (SolicitacaoProfAdapter.SolicitacaoProfViewHolder) holder;
        solicitacaoProfViewHolder.nome.setText(nome);
        solicitacaoProfViewHolder.descricao.setText(descricao);

        Date dataDate = data.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(dataDate);
        solicitacaoProfViewHolder.data.setText(formattedDate);

        Date horaDate = hora.toDate();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTime = timeFormat.format(horaDate);
        solicitacaoProfViewHolder.hora.setText(formattedTime);

        if (status == 0) {
            solicitacaoProfViewHolder.status.setText("Pendente");
            solicitacaoProfViewHolder.status.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.amarelo)); // Amarelo
        } else if (status == 1) {
            solicitacaoProfViewHolder.status.setText("Aceito");
        } else if (status == 2) {
            solicitacaoProfViewHolder.status.setText("Recusado");
            solicitacaoProfViewHolder.status.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red)); // Vermelho
        }

    }

    @Override
    public int getItemCount() {
        return reservaProfList.size();
    }

    public static class SolicitacaoProfViewHolder extends RecyclerView.ViewHolder {
        TextView nome, descricao, data, hora, status;


        public SolicitacaoProfViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.texto_titulo_reserva);
            descricao = itemView.findViewById(R.id.texto_descricao_reserva);
            data = itemView.findViewById(R.id.data);
            hora = itemView.findViewById(R.id.text_room_location);
            status = itemView.findViewById(R.id.text_room_status);
        }

    }
}
