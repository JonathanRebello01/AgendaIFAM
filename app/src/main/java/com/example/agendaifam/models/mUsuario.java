package com.example.agendaifam.models;

public class mUsuario {
    private String nome;
    private String email;
    private Integer codigo;
    private String tipoConta;
    private String areaGestao;

    public mUsuario(String nome, String email, Integer codigo) {
        this.nome = nome;
        this.email = email;
        this.codigo = codigo;
    }

    public mUsuario(String nome, String email, Integer codigo, String areaGestao, String tipoConta) {
        this.nome = nome;
        this.email = email;
        this.codigo = codigo;
        this.tipoConta = tipoConta;
        this.areaGestao = areaGestao;
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

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getAreaGestao() {
        return areaGestao;
    }

    public void setAreaGestao(String areaGestao) {
        this.areaGestao = areaGestao;
    }

    @Override
    public String toString() {
        return "mUsuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", codigo=" + codigo +
                ", tipoConta='" + tipoConta + '\'' +
                ", areaGestao='" + areaGestao + '\'' +
                '}';
    }
}
