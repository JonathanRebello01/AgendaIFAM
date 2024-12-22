package com.example.agendaifam.models;


import com.google.firebase.Timestamp;

public class mReserva {
    private Timestamp horaInicioReserva;
    private Timestamp horaFimReserva;
    private Timestamp dataReserva;
    private String idEspacosReserva;
    private String idProfessorReserva;
    private  String idGestorReserva;
    private String idReserva;
    private  String nomeProfessorReserva;
    private  String nomeGestorReserva;
    private String nomeEspaco;
    private String descricaoEspaco;
    private int statusReserva;
    private int codigoSetor;


    @Override
    public String toString() {
        return "mReserva{" +
                "horaInicioReserva=" + horaInicioReserva +
                ", horaFimReserva=" + horaFimReserva +
                ", dataReserva=" + dataReserva +
                ", idEspacosReserva='" + idEspacosReserva + '\'' +
                ", idProfessorReserva='" + idProfessorReserva + '\'' +
                ", idGestorReserva='" + idGestorReserva + '\'' +
                ", idReserva='" + idReserva + '\'' +
                ", nomeProfessorReserva='" + nomeProfessorReserva + '\'' +
                ", nomeGestorReserva='" + nomeGestorReserva + '\'' +
                ", nomeEspaco='" + nomeEspaco + '\'' +
                ", descricaoEspaco='" + descricaoEspaco + '\'' +
                ", statusReserva=" + statusReserva +
                ", codigoSetor=" + codigoSetor +
                '}';
    }

    public mReserva(){

    }
    public mReserva(Timestamp horaInicioReserva, Timestamp horaFimReserva, Timestamp dataReserva, String idEspacosReserva, String idProfessorReserva, String idGestorReserva, String idReserva, String nomeProfessorReserva, String nomeGestorReserva, String nomeEspaco, String descricaoEspaco, int statusReserva, int codigoSetor) {
        this.horaInicioReserva = horaInicioReserva;
        this.horaFimReserva = horaFimReserva;
        this.dataReserva = dataReserva;
        this.idEspacosReserva = idEspacosReserva;
        this.idProfessorReserva = idProfessorReserva;
        this.idGestorReserva = idGestorReserva;
        this.idReserva = idReserva;
        this.nomeProfessorReserva = nomeProfessorReserva;
        this.nomeGestorReserva = nomeGestorReserva;
        this.nomeEspaco = nomeEspaco;
        this.descricaoEspaco = descricaoEspaco;
        this.statusReserva = statusReserva;
        this.codigoSetor = codigoSetor;
    }

    public Timestamp getHoraInicioReserva() {
        return horaInicioReserva;
    }

    public void setHoraInicioReserva(Timestamp horaInicioReserva) {
        this.horaInicioReserva = horaInicioReserva;
    }

    public Timestamp getHoraFimReserva() {
        return horaFimReserva;
    }

    public void setHoraFimReserva(Timestamp horaFimReserva) {
        this.horaFimReserva = horaFimReserva;
    }

    public Timestamp getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Timestamp dataReserva) {
        this.dataReserva = dataReserva;
    }

    public String getIdEspacosReserva() {
        return idEspacosReserva;
    }

    public void setIdEspacosReserva(String idEspacosReserva) {
        this.idEspacosReserva = idEspacosReserva;
    }

    public String getIdProfessorReserva() {
        return idProfessorReserva;
    }

    public void setIdProfessorReserva(String idProfessorReserva) {
        this.idProfessorReserva = idProfessorReserva;
    }

    public String getIdGestorReserva() {
        return idGestorReserva;
    }

    public void setIdGestorReserva(String idGestorReserva) {
        this.idGestorReserva = idGestorReserva;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getNomeProfessorReserva() {
        return nomeProfessorReserva;
    }

    public void setNomeProfessorReserva(String nomeProfessorReserva) {
        this.nomeProfessorReserva = nomeProfessorReserva;
    }

    public String getNomeGestorReserva() {
        return nomeGestorReserva;
    }

    public void setNomeGestorReserva(String nomeGestorReserva) {
        this.nomeGestorReserva = nomeGestorReserva;
    }

    public String getNomeEspaco() {
        return nomeEspaco;
    }

    public void setNomeEspaco(String nomeEspaco) {
        this.nomeEspaco = nomeEspaco;
    }

    public String getDescricaoEspaco() {
        return descricaoEspaco;
    }

    public void setDescricaoEspaco(String descricaoEspaco) {
        this.descricaoEspaco = descricaoEspaco;
    }

    public int getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva(int statusReserva) {
        this.statusReserva = statusReserva;
    }

    public int getCodigoSetor() {
        return codigoSetor;
    }

    public void setCodigoSetor(int codigoSetor) {
        this.codigoSetor = codigoSetor;
    }
}
