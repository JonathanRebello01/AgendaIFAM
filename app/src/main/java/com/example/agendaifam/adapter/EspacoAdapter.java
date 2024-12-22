package com.example.agendaifam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaifam.R;
import com.example.agendaifam.models.mEspacos;

import java.text.BreakIterator;
import java.util.List;

public class EspacoAdapter extends RecyclerView.Adapter  {

    private List<mEspacos> espacoList;

    public EspacoAdapter(List<mEspacos> espacoList) {
        this.espacoList = espacoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.locais_item, parent, false);

        return new EspacoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String nome = espacoList.get(position).getNome();
        String descricao = espacoList.get(position).getDescricao();
        EspacoViewHolder.nome.setText(nome);
        EspacoViewHolder.descricao.setText(descricao);
    }

    @Override
    public int getItemCount() {
        return espacoList.size();
    }

    public static class EspacoViewHolder extends RecyclerView.ViewHolder {
        static TextView nome;
        static TextView descricao;

        public EspacoViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tv_title);
            descricao = itemView.findViewById(R.id.tv_description);
        }

    }

}
