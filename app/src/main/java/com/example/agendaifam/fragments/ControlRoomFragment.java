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
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.agendaifam.R;
import com.example.agendaifam.adapter.EspacoAdapter;
import com.example.agendaifam.models.mEspacos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlRoomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rv_espaco;
    private Button add_espaco;
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();
    private String usuarioID;

    public ControlRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlRoom.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlRoomFragment newInstance(String param1, String param2) {
        ControlRoomFragment fragment = new ControlRoomFragment();
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
        return inflater.inflate(R.layout.fragment_control_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniciarComponentes();

        add_espaco.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_control_room, new adicionar_espacos()).commit();
            }
        });
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ContaPrefs", Context.MODE_PRIVATE);

        int codigo = sharedPreferences.getInt("codigo", 0); // Valor padrão se não existir
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (codigo != 0) {
            List<mEspacos> espacoList = new ArrayList<>();

            CollectionReference getdata = banco_recuperar.collection("espaco" + "/" + codigo + "/" + "data");

            getdata.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mEspacos espaco = document.toObject(mEspacos.class);
                            espacoList.add(espaco);
                        }
                        rv_espaco.setAdapter(new EspacoAdapter(espacoList, requireContext()));
                        rv_espaco.setLayoutManager(new LinearLayoutManager(getContext()));

                    } else {
                        Log.d("Firestore", "Erro ao recuperar documentos: ", task.getException());
                    }
                }
            });
        }else {
            Toast.makeText(getContext(), "Você não tem permissão para acessar esse recurso", Toast.LENGTH_LONG).show();
        }
    }

    private void iniciarComponentes(){
        rv_espaco = requireView().findViewById(R.id.rv_locais);
        add_espaco = requireView().findViewById(R.id.button_add_espaco);
    }

}