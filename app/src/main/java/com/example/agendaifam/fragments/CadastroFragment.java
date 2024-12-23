package com.example.agendaifam.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaifam.GestorActivity;
import com.example.agendaifam.ProfessorActivity;
import com.example.agendaifam.R;
import com.example.agendaifam.models.mProfessor;
import com.example.agendaifam.models.mUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
    private EditText edit_nome,edit_email,edit_senha, edt_codigo;
    private Button cadastrar;
    SwitchCompat tipoConta;
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();

    private FirebaseAuth auth;

    String[] mensagens = {"Preencha todos os campo", "Cadastro realizado com sucesso!"};
    String  usuarioID;

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
        tipoConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipoConta.isChecked()){
                    edt_codigo.setVisibility(View.VISIBLE);
                }
                else {
                    edt_codigo.setVisibility(View.GONE);
                }
            }
        });
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
                            SalvaDadosUsuario();
                            edit_nome.setText("");
                            edit_email.setText("");
                            edit_senha.setText("");
                            edt_codigo.setText("");
                            Toast.makeText(requireContext(), mensagens[1], Toast.LENGTH_LONG).show();
                        } else {
                            String erro;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erro = "Digite uma senha com no mínimo 6 caracteres";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = "Essa conta já foi cadastrada";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "E-mail inválido";
                            } catch (Exception e) {
                                erro = "Erro ao cadastrar usuário: " + e.getMessage();
                            }
                            Toast.makeText(requireContext(), erro, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private  void SalvaDadosUsuario(){
        String email = edit_email.getText().toString().trim();
        String nome = edit_nome.getText().toString().trim();
        int codigo;
        if(!edt_codigo.getText().toString().trim().equals("") && Integer.parseInt(edt_codigo.getText().toString().trim()) != 0) {
            codigo = Integer.parseInt(edt_codigo.getText().toString());
        }
        else {
            codigo = 0;
        }
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUsuario usuario;

        if (codigo != 0) {
            usuario = new mUsuario(nome, email, codigo);

            usuario.setTipoConta("gestor");
            DocumentReference getArea = banco_recuperar.collection("idGestao").document("codigos");
            getArea.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                String areaGestao;
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (codigo == 1){
                        areaGestao = value.getString("1");
                    }
                    else if (codigo == 2){
                        areaGestao = value.getString("2");
                    }
                    else if (codigo == 3) {
                        areaGestao = value.getString("3");
                    }
                    else {
                        areaGestao = null;
                    }

                    usuario.setAreaGestao(areaGestao);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
                    documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("db", "Sucesso ao salvar os dados");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("db_error", "Erro ao salvar os dados" + e.toString());

                                }
                            });


                }
            });
        }
        else {
            usuario = new mUsuario(nome, email, codigo);
            usuario.setTipoConta("professor");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

            documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("db", "Sucesso ao salvar os dados");

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("db_error", "Erro ao salvar os dados" + e.toString());

                }
            });

        }
    }


    private void iniciarcomponentes(View view){
        login = getView().findViewById(R.id.ir_para_login);
        cadastrar = getView().findViewById(R.id.cadastrar);
        edit_nome = getView().findViewById(R.id.input_nome_cadastro);
        edit_email = getView().findViewById(R.id.input_email_cadastro);
        edit_senha = getView().findViewById(R.id.input_senha_cadastro);
        tipoConta = getView().findViewById(R.id.tipoConta);
        edt_codigo = getView().findViewById(R.id.input_area_cadastro);
    }
}


