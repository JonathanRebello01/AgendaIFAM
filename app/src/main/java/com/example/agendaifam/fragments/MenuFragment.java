package com.example.agendaifam.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.agendaifam.GestorActivity;
import com.example.agendaifam.ProfessorActivity;
import com.example.agendaifam.R;
import com.example.agendaifam.models.mUsuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context ctx;
    private TextView tv_email;
    private TextView tv_cargo;
    private TextView tv_nome;
    private String usuarioID;
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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
        ctx = requireContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarComponentes();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference getdata = banco_recuperar.collection("Usuarios").document(usuarioID);
        getdata.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    Long codigoLong = (Long) Objects.requireNonNull(value.getData()).get("codigo");
                    Integer codigo = codigoLong != null ? codigoLong.intValue() : 0;
                    String email = value.getString("email");
                    String nome = value.getString("nome");
                    String tipoConta;

                    if (codigo != 0 && codigo != null){
                        tipoConta = "Gestor";
                    }
                    else {
                        tipoConta = "Professor";
                    }

                    DocumentReference getArea = banco_recuperar.collection("idGestao").document("codigos");
                    getArea.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        String areaGestao;

                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (codigo == 1) {
                                areaGestao = value.getString("1");
                            } else if (codigo == 2) {
                                areaGestao = value.getString("2");
                            } else if (codigo == 3) {
                                areaGestao = value.getString("3");
                            } else if (codigo == 4) {
                                areaGestao = value.getString("4");
                            } else if (codigo == 5) {
                                areaGestao = value.getString("5");
                            } else if (codigo == 6) {
                                areaGestao = value.getString("6");
                            } else if (codigo == 7) {
                                areaGestao = value.getString("7");
                            } else if (codigo == 8) {
                                areaGestao = value.getString("8");
                            } else if (codigo == 9) {
                                areaGestao = value.getString("9");
                            } else if (codigo == 10) {
                                areaGestao = value.getString("10");
                            } else {
                                areaGestao = null;
                            }

                            tv_email.setText(email);
                            tv_nome.setText(nome);
                            if (areaGestao != null) {
                                tv_cargo.setText("Gestor: " + areaGestao);
                            }
                            else {
                                tv_cargo.setText("Professor");
                            }
                        }
                    });
                }
            }
        });
    }
    private void iniciarComponentes(){
        tv_nome = requireView().findViewById(R.id.user_name);
        tv_email = requireView().findViewById(R.id.user_email);
        tv_cargo = requireView().findViewById(R.id.user_role);

    }
}