package com.example.agendaifam.models;

public class mProfessor {
    private String nome;
    private String email;

    public mProfessor(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public mProfessor() {

    }

    @Override
    public String toString() {
        return "mProfessor{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
