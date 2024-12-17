package com.example.agendaifam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaifam.R;
import com.example.agendaifam.models.mEspacos;

import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter {

    private List<mEspacos> espacoList;

    public ReservaAdapter(List<mEspacos> espacoList) {
        this.espacoList = espacoList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.item_reserva, parent, false);

        return new ReservaAdapter.ReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String nome = espacoList.get(position).getNome();
        String descricao = espacoList.get(position).getDescricao();
        ReservaAdapter.ReservaViewHolder.nome_reservar.setText(nome);
        ReservaAdapter.ReservaViewHolder.descricao_reservar.setText(descricao);
    }

    @Override
    public int getItemCount() {
        return espacoList.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        static TextView nome_reservar;
        static TextView descricao_reservar;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_reservar = itemView.findViewById(R.id.texto_titulo_reserva);
            descricao_reservar = itemView.findViewById(R.id.texto_descricao_reserva);
        }

    }

}
