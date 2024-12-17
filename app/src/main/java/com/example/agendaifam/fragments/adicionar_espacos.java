package com.example.agendaifam.fragments;

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
import com.example.agendaifam.models.mEspacos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link adicionar_espacos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class adicionar_espacos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText nome, descricao, edt_cod_departamento;
    private Button confirmar;
    private int cod_departamento;
    private String usuarioID;

    public adicionar_espacos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sdicionar_espacos.
     */
    // TODO: Rename and change types and number of parameters
    public adicionar_espacos newInstance(String param1, String param2) {
        adicionar_espacos fragment = new adicionar_espacos();
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
        return inflater.inflate(R.layout.fragment_adicionar_espacos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarComponentes();

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            salvarDadosespaco(nome, descricao, edt_cod_departamento);
            }
        });


    }

    public void salvarDadosespaco(EditText nome, EditText  descricao, EditText cod_departamento){
        String nome_espaco = nome.getText().toString();
        String descricao_espaco = descricao.getText().toString();
        int codigo = Integer.parseInt(cod_departamento.getText().toString());

        mEspacos espaco = new mEspacos(nome_espaco, descricao_espaco, null, null, codigo);

        FirebaseFirestore banco = FirebaseFirestore.getInstance();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference documentReference = banco.collection("espaco").document(String.valueOf(codigo)).collection("data");
        documentReference.add(espaco).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
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
        edt_cod_departamento = requireView().findViewById(R.id.edit_cod_departamento);

        confirmar = requireView().findViewById(R.id.button_confirm);

    }
}