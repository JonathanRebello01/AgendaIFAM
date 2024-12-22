package com.example.agendaifam.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.agendaifam.R;
import com.example.agendaifam.adapter.SolicitacaoGestorAdapter;
import com.example.agendaifam.adapter.SolicitacaoProfAdapter;
import com.example.agendaifam.models.mReserva;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SolicitationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SolicitationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rv_solicitacao;
    private String usuarioID;
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();

    public SolicitationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SolicitationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SolicitationFragment newInstance(String param1, String param2) {
        SolicitationFragment fragment = new SolicitationFragment();
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
        return inflater.inflate(R.layout.fragment_solicitation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarComponentes();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ContaPrefs", Context.MODE_PRIVATE);
        int codigo_perfil = sharedPreferences.getInt("codigo", 0); // Valor padrão se não existir

        if (codigo_perfil != 0) {
            usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            List<mReserva> reservaList = new ArrayList<>();
            // Referência à coleção onde estão armazenados os códigos
            CollectionReference solicitacoes = banco_recuperar.collection("reservas");

            solicitacoes.get().addOnCompleteListener(taskReserva -> {
                if (taskReserva.isSuccessful()) {
                    String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    for (QueryDocumentSnapshot reservaDoc : taskReserva.getResult()) {

                        mReserva reserva = reservaDoc.toObject(mReserva.class);

                        System.out.println("usuarioID: " + usuarioID);
                        System.out.println("idProf: " + reserva.getIdProfessorReserva());

                        if (Objects.equals(reserva.getCodigoSetor(), codigo_perfil)) {
                            reservaList.add(reserva);
                        }
                        rv_solicitacao.setAdapter(new SolicitacaoGestorAdapter(reservaList));
                        rv_solicitacao.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Você não tem permissão para acessar esse recurso", Toast.LENGTH_LONG).show();
        }
    }

    private void iniciarComponentes() {
        rv_solicitacao = requireView().findViewById(R.id.rv_solicitacao);
    }
}