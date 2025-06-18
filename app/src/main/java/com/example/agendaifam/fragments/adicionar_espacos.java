package com.example.agendaifam.fragments;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agendaifam.R;
import com.example.agendaifam.adapter.EspacoAdapter;
import com.example.agendaifam.models.mEspacos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class adicionar_espacos extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText nome, descricao, edt_cod_departamento;
    private Button confirmar;
    private Context ctx;
    private int codigoPerfil;
    private String idEditando;
    private boolean isEditing;


    public adicionar_espacos() {
    }

    public static adicionar_espacos newInstance(mEspacos espaco) {
        adicionar_espacos fragment = new adicionar_espacos();
        Bundle args = new Bundle();
        args.putString("id", espaco.getId());
        args.putString("nome", espaco.getNome());
        args.putString("descricao", espaco.getDescricao());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = requireContext();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("ContaPrefs", Context.MODE_PRIVATE);
        int codigo = sharedPreferences.getInt("codigo", 0);
        codigoPerfil = codigo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Bundle args = getArguments();
//        iniciarComponentes();
//        if (args != null) {
//            String id = args.getString("id");
//            String new_nome = args.getString("nome");
//            String new_descricao = args.getString("descricao");
//
//            nome.setText(new_nome);
//            descricao.setText(new_descricao);
//
//            isEditing = true;
//            idEditando = id ;
//        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adicionar_espacos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarComponentes();

        Bundle args = getArguments();
        if (args != null) {
            String id = args.getString("id");
            String new_nome = args.getString("nome");
            String new_descricao = args.getString("descricao");

            nome.setText(new_nome);
            descricao.setText(new_descricao);

            isEditing = true;
            idEditando = id;
        }

        edt_cod_departamento.setHint(String.valueOf(codigoPerfil));
        edt_cod_departamento.setFocusable(false);
        edt_cod_departamento.setClickable(false);

        if (isEditing) {
            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore banco = FirebaseFirestore.getInstance();


                    CollectionReference collectionReference = banco.collection("espaco").document(String.valueOf(codigoPerfil)).collection("data");

                    String nm = nome.getText().toString();
                    String desc = descricao.getText().toString();

                    mEspacos espaco = new mEspacos(nm, desc, null, idEditando, codigoPerfil);
                    collectionReference.document(idEditando).set(espaco).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("db", "Sucesso ao editar os dados");

                            Toast.makeText(requireContext(), "Sucesso ao editar dados!", Toast.LENGTH_LONG).show();

                            nome.setText("");
                            descricao.setText("");
                            edt_cod_departamento.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db", "Erro ao editar os dados: " + e);
                            Toast.makeText(requireContext(), "ERRO ao editar dados!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } else {
            confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nm = nome.getText().toString();
                    String desc = descricao.getText().toString();
                    String cod = edt_cod_departamento.getText().toString();
                    if (nm.isEmpty() || desc.isEmpty()) {
                        Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
                    } else {
                        salvarDadosespaco(nome, descricao, codigoPerfil);
                    }
                }
            });
        }

    }

    public void salvarDadosespaco(EditText nome, EditText  descricao, int codigoPerfil){
        String nome_espaco = nome.getText().toString();
        String descricao_espaco = descricao.getText().toString();


        FirebaseFirestore banco = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = banco.collection("espaco").document(String.valueOf(codigoPerfil)).collection("data");
        String documentId = collectionReference.document().getId();

        mEspacos espaco = new mEspacos(nome_espaco, descricao_espaco, null, documentId, codigoPerfil);

        collectionReference.document(documentId).set(espaco).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar os dados");

                Toast.makeText(requireContext(), "Sucesso ao salvar dados!", Toast.LENGTH_LONG).show();

                nome.setText("");
                descricao.setText("");
                edt_cod_departamento.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db", "Erro ao salvar os dados: " + e);
                Toast.makeText(requireContext(), "ERRO ao salvar dados!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void iniciarComponentes(){
        nome = requireView().findViewById(R.id.edit_space_name);
        descricao = requireView().findViewById(R.id.edit_description);
        edt_cod_departamento = requireView().findViewById(R.id.add_cod_departamento);

        confirmar = requireView().findViewById(R.id.button_confirm);

    }
}