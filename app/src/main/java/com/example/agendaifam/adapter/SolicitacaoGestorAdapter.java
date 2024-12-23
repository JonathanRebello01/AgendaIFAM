package com.example.agendaifam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaifam.R;
import com.example.agendaifam.models.mReserva;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SolicitacaoGestorAdapter extends RecyclerView.Adapter{

    private List<mReserva> reservaGestList;

    public SolicitacaoGestorAdapter(List<mReserva> reservaList) {
        if (reservaList == null) {
            this.reservaGestList = new ArrayList<>();
        } else {
            this.reservaGestList = reservaList;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View itemView = layoutInflater.inflate(R.layout.solicitacao_item, parent, false);

        return new SolicitacaoGestorAdapter.SolicitacaoGestorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String nome = reservaGestList.get(position).getNomeEspaco();
        String descricao = reservaGestList.get(position).getDescricaoEspaco();
        Timestamp data = reservaGestList.get(position).getDataReserva();
        Timestamp hora = reservaGestList.get(position).getHoraInicioReserva();
        String nome_solicitante = reservaGestList.get(position).getNomeProfessorReserva();
        String nomeEesp = reservaGestList.get(position).getNomeEspaco();

        ImageButton btn_sim = holder.itemView.findViewById(R.id.btn_sim);
        ImageButton btn_nao = holder.itemView.findViewById(R.id.btn_nao);
        TextView descricao_solicitacao = holder.itemView.findViewById(R.id.descricao_solicitacao);

        if (reservaGestList.get(position).getStatusReserva() == 0){
            btn_sim.setVisibility(View.VISIBLE);
            btn_nao.setVisibility(View.VISIBLE);
        }else if (reservaGestList.get(position).getStatusReserva() == 1){
            btn_sim.setVisibility(View.VISIBLE);
            btn_sim.setClickable(false);
            btn_sim.setAlpha(0.5f);
            btn_sim.setEnabled(false);
            btn_sim.setFocusable(false);
            btn_nao.setVisibility(View.INVISIBLE);
            descricao_solicitacao.setVisibility(View.VISIBLE);
            descricao_solicitacao.setText("Aceito");
        }else if (reservaGestList.get(position).getStatusReserva() == 2){
            btn_sim.setVisibility(View.INVISIBLE);
            btn_nao.setVisibility(View.VISIBLE);
            btn_nao.setClickable(false);
            btn_nao.setAlpha(0.5f);
            btn_nao.setEnabled(false);
            btn_nao.setFocusable(false);
            descricao_solicitacao.setVisibility(View.VISIBLE);
            descricao_solicitacao.setText("Rejeitado");
        }



        SolicitacaoGestorAdapter.SolicitacaoGestorViewHolder solicitacaoGestorViewHolder = (SolicitacaoGestorAdapter.SolicitacaoGestorViewHolder) holder;
        solicitacaoGestorViewHolder.nome.setText(nome);
        solicitacaoGestorViewHolder.descricao.setText(descricao);

        Date dataDate = data.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(dataDate);
        solicitacaoGestorViewHolder.data.setText(formattedDate);

        Date horaDate = hora.toDate();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String formattedTime = timeFormat.format(horaDate);
        solicitacaoGestorViewHolder.hora.setText(formattedTime);

        solicitacaoGestorViewHolder.nome_solicitante.setText(nome_solicitante);
        solicitacaoGestorViewHolder.nomeEesp.setText(nomeEesp);


        solicitacaoGestorViewHolder.confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("reservas");
                mReserva reservaAtual = reservaGestList.get(position);
                reservaAtual.setStatusReserva(1);
                collectionReference.document(reservaAtual.getIdReserva()).set(reservaAtual).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(v.getContext(), "Reserva confirmada com sucesso!", Toast.LENGTH_SHORT).show();
                        notifyItemChanged(position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Erro ao confirmar a reserva!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        solicitacaoGestorViewHolder.rejeitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("reservas");
                mReserva reservaAtual = reservaGestList.get(position);
                reservaAtual.setStatusReserva(2);
                collectionReference.document(reservaAtual.getIdReserva()).set(reservaAtual).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(v.getContext(), "Reserva rejeitada com sucesso!", Toast.LENGTH_SHORT).show();
                        notifyItemChanged(position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Erro ao rejeitar a reserva!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservaGestList.size();
    }

    public static class SolicitacaoGestorViewHolder extends RecyclerView.ViewHolder {
        TextView nome, descricao, data, hora, nome_solicitante, nomeEesp, descricao_solicitacao;
        ImageButton confirmar, rejeitar;



        public SolicitacaoGestorViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nome_solicitacao_gest_iten);
            descricao = itemView.findViewById(R.id.descricao_local_solicitacao_gest_iten);
            data = itemView.findViewById(R.id.data_dolicitacao_gest);
            hora = itemView.findViewById(R.id.hora_iten_solicitacao_gest);
            nome_solicitante = itemView.findViewById(R.id.nome);
            nomeEesp = itemView.findViewById(R.id.nomeevento);
            confirmar = itemView.findViewById(R.id.btn_sim);
            rejeitar = itemView.findViewById(R.id.btn_nao);
            descricao_solicitacao = itemView.findViewById(R.id.descricao_solicitacao);
        }

    }

}
