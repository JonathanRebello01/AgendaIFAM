package com.example.agendaifam.models;

public class mEspacos {
    private String nome;
    private String descricao;
    private String local;
    private String horario;

    public mEspacos(String nome, String descricao, String local, String horario) {
        this.nome = nome;
        this.descricao = descricao;
        this.local = local;
        this.horario = horario;
    }

    public mEspacos() {

    }

    @Override
    public String toString() {
        return "mEspacos{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", local='" + local + '\'' +
                ", horario='" + horario + '\'' +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
