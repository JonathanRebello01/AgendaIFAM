package com.example.agendaifam.adapter;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaifam.R;
import com.example.agendaifam.fragments.adicionar_espacos;
import com.example.agendaifam.models.mEspacos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EspacoAdapter extends RecyclerView.Adapter  {

    private List<mEspacos> espacoList;
    private Context ctx;

    public EspacoAdapter(List<mEspacos> espacoList, Context context) {
        this.espacoList = espacoList;
        this.ctx = context;
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

        EspacoViewHolder.edit.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            mEspacos espacoAtual = espacoList.get(pos);

            adicionar_espacos fragment = adicionar_espacos.newInstance(espacoAtual);

            // Troca para o fragment de edição
            ((AppCompatActivity) ctx).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_control_room, fragment) // ajuste para o ID do seu container
                    .addToBackStack(null)
                    .commit();
        });

//        EspacoViewHolder viewHolder = (EspacoViewHolder) holder;

        EspacoViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                mEspacos espacoAtual = espacoList.get(pos);
                String documentIdAtual = espacoAtual.getId();

                FirebaseFirestore banco = FirebaseFirestore.getInstance();
                SharedPreferences sharedPreferences = ctx.getSharedPreferences("ContaPrefs", Context.MODE_PRIVATE);
                int codigo = sharedPreferences.getInt("codigo", 0);

                CollectionReference collectionReference = banco.collection("espaco")
                        .document(String.valueOf(codigo))
                        .collection("data");

                collectionReference.document(documentIdAtual).delete()
                        .addOnSuccessListener(unused -> {
                            espacoList.remove(pos);
                            notifyItemRemoved(pos);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("db", "Erro ao deletar", e);
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return espacoList.size();
    }

    public static class EspacoViewHolder extends RecyclerView.ViewHolder {
        static TextView nome;
        static TextView descricao;
        static ImageButton edit;
        static ImageButton delete;

        public EspacoViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tv_title);
            descricao = itemView.findViewById(R.id.tv_description);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
        }

    }

}
