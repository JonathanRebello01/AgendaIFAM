package com.example.agendaifam.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendaifam.GestorActivity;
import com.example.agendaifam.ProfessorActivity;
import com.example.agendaifam.R;
import com.example.agendaifam.models.mUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextInputEditText email, senha;
    private TextView cadastro, recuperarSenha;
    private Button entrar;
    private String usuarioID;
    Context ctx;
    private final FirebaseFirestore banco_recuperar = FirebaseFirestore.getInstance();
    private ImageView  visualizar_senha;
    boolean password_invisible = true;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = requireContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarComponentes(view);
        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.main, new CadastroFragment()).commit();
            }
        });
        recuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.main, new RedefinirSenhaFragment()).commit();
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailtext = email.getText().toString();
                String senhaText = senha.getText().toString();
                if(emailtext.isEmpty() || senhaText.isEmpty()){
                    Toast.makeText(requireContext(), "Preencha TODOS os campos ; )", Toast.LENGTH_SHORT).show();
                }
                else {
                    autenticarUsuario(emailtext, senhaText);
                }
            }
        });
        visualizar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password_invisible){
                    senha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password_invisible = false;
                }

                else {
                    senha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_invisible = true;
                }
            }
        });
    }

    private void autenticarUsuario(String email, String senha) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DocumentReference getdata = banco_recuperar.collection("Usuarios").document(usuarioID);
                    getdata.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(value != null){
                                Long codigoLong = (Long) Objects.requireNonNull(value.getData()).get("codigo");
                                Integer codigo = codigoLong != null ? codigoLong.intValue() : 0; // Converte Long para int
                                String email = value.getString("email");
                                String nome = value.getString("nome");
                                String tipoConta;

                                if (codigo != 0 && codigo != null){
                                    tipoConta = "gestor";
                                }
                                else {
                                    tipoConta = "professor";
                                }
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


                                        mUsuario usuario = new mUsuario(nome, email, codigo, areaGestao, tipoConta);

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
                                        documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("db", "Sucesso ao salvar os dados");
                                                        SharedPreferences sharedPreferences = ctx.getSharedPreferences("ContaPrefs", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                                        // Salvando dados de diferentes tipos
                                                        editor.putString("nome", nome);
                                                        editor.putString("email", email);
                                                        editor.putInt("codigo", codigo);
                                                        editor.putString("areagestao", areaGestao);
                                                        editor.putString("tipoconta", tipoConta);

                                                        editor.apply();

                                                        if (tipoConta.equals("gestor")){
                                                            Intent intent = new Intent(ctx, GestorActivity.class);
                                                            startActivity(intent);
                                                        }else {
                                                            Intent intent = new Intent(ctx, ProfessorActivity.class);
                                                            startActivity(intent);
                                                        }

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
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Erro na tentativa de login" + e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void iniciarComponentes(View view){
        cadastro = view.findViewById(R.id.catastre_se);
        recuperarSenha = view.findViewById(R.id.esqueci_minha_senha);
        entrar = view.findViewById(R.id.login);

        email = view.findViewById(R.id.input_email_login);
        senha = view.findViewById(R.id.input_senha_login);

        visualizar_senha = view.findViewById(R.id.visualizar_senha_login);
    }

}