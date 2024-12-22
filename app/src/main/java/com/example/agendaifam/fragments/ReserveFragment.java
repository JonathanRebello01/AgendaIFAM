package com.example.agendaifam.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.agendaifam.R;
import com.example.agendaifam.adapter.EspacoAdapter;
import com.example.agendaifam.adapter.ReservaAdapter;
import com.example.agendaifam.models.mEspacos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReserveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReserveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rv_reservaSalas;
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();
    private String usuarioID;

    public ReserveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReserveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReserveFragment newInstance(String param1, String param2) {
        ReserveFragment fragment = new ReserveFragment();
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
        return inflater.inflate(R.layout.fragment_reserve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarComponentes();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ContaPrefs", Context.MODE_PRIVATE);
        int codigo_perfil = sharedPreferences.getInt("codigo", 0); // Valor padrão se não existir

        if (codigo_perfil == 0) {
            usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            List<mEspacos> espacoList = new ArrayList<>();

            // Referência à coleção onde estão armazenados os códigos
            DocumentReference codigosRef = banco_recuperar.collection("idGestao").document("codigos");

            codigosRef.get().addOnCompleteListener(taskCodigos -> {
                if (taskCodigos.isSuccessful()) {

                    Map<String, Object> codigosMap = taskCodigos.getResult().getData();
                    ArrayList<String> cod = new ArrayList<>();
                    // Verificando o conteúdo do Map
                    if (codigosMap != null) {
                        for (Map.Entry<String, Object> entry : codigosMap.entrySet()) {
                            String chave = entry.getKey();
                            String valor = (String) entry.getValue();  // Exemplo: "area1", "area2", "area3"
                            cod.add(chave);
                            Log.d("Firestore", "Chave: " + chave + ", Valor: " + valor);
                        }
                    }
                    final int[] consultasPendentes = {cod.size()}; // Variável para contar as consultas pendentes

                    // Agora vamos fazer as consultas para cada código
                    for (String codigo : cod) {
                        System.out.println("teste "+ codigo);
                        // Para cada código, fazemos a consulta nos espaços
                        CollectionReference espacoRef = banco_recuperar.collection("espaco").document(codigo).collection("data");


                        espacoRef.get().addOnCompleteListener(taskEspacos -> {

                            if (taskEspacos.isSuccessful()) {
                                for (QueryDocumentSnapshot espacoDoc : taskEspacos.getResult()) {
                                    mEspacos espaco = espacoDoc.toObject(mEspacos.class);
                                    espacoList.add(espaco); // Adiciona o espaço à lista
                                }
                            } else {
                                Log.d("Firestore", "Erro ao recuperar espaços: ", taskEspacos.getException());
                            }

                            // Decrementa o contador após cada consulta
                            consultasPendentes[0]--;

                            // Se todas as consultas forem concluídas, atualize o RecyclerView
                            if (consultasPendentes[0] == 0) {
                                rv_reservaSalas.setAdapter(new ReservaAdapter(espacoList, requireContext()));
                                rv_reservaSalas.setLayoutManager(new LinearLayoutManager(getContext()));
                            }
                        });
                    }

                } else {
                    Log.d("Firestore", "Erro ao recuperar códigos: ", taskCodigos.getException());
                }
            });
        } else {
            Toast.makeText(getContext(), "Você não tem permissão para acessar esse recurso", Toast.LENGTH_LONG).show();
        }
    }


    private void iniciarComponentes(){
        rv_reservaSalas = requireView().findViewById(R.id.rv_reservas);
    }

}