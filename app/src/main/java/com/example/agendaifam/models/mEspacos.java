package com.example.agendaifam.models;

public class mEspacos {
    private String nome;
    private String descricao;
    private String horario;
    private String id;
    private int cod_departamento;


    public mEspacos(String nome, String descricao, String horario, String id, int cod_departamento) {
        this.nome = nome;
        this.descricao = descricao;
        this.horario = horario;
        this.id = id;
        this.cod_departamento = cod_departamento;
    }

    public mEspacos() {

    }



    @Override
    public String toString() {
        return "mEspacos{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", horario='" + horario + '\'' +
                ", id='" + id + '\'' +
                ", departamento='" + cod_departamento + '\'' +
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCod_departamento() {
        return cod_departamento;
    }

    public void setCod_departamento(int cod_departamento) {
        this.cod_departamento = cod_departamento;
    }
}
