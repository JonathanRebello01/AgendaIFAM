package com.example.agendaifam.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaifam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CadastroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadastroFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView login;
    private EditText edit_nome,edit_email,edit_senha;
    private Button cadastrar;

    private FirebaseAuth auth;

    String[] mensagens = {"Preencha todos os campo", "Cadastro realizado com sucesso!"};

    public CadastroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CadastroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CadastroFragment newInstance(String param1, String param2) {
        CadastroFragment fragment = new CadastroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
//        private FirebaseAuth auth1;
//        private EditText editEmail,edit_email, editSenha;
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
        return inflater.inflate(R.layout.fragment_cadastro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        iniciarcomponentes(view);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main, new LoginFragment()).commit();
            }
        });
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = edit_nome.getText().toString().trim();
                String email = edit_email.getText().toString().trim();
                String senha = edit_senha.getText().toString().trim();

                if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(requireContext(), mensagens[0], Toast.LENGTH_LONG).show();
                }else{
                    CadastrarUsuario();
                }
            }
        });
    }

    private void CadastrarUsuario() {
        String email = edit_email.getText().toString().trim();
        String senha = edit_senha.getText().toString().trim();


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), mensagens[1], Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(requireContext(), "Erro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void iniciarcomponentes(View view){
        login = getView().findViewById(R.id.ir_para_login);
        cadastrar = getView().findViewById(R.id.cadastrar);
        edit_nome = getView().findViewById(R.id.input_nome_cadastro);
        edit_email = getView().findViewById(R.id.input_email_cadastro);
        edit_senha = getView().findViewById(R.id.input_senha_cadastro);
    }
}


