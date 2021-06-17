package com.example.myapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Divida {
    @PrimaryKey
    Integer id;
    @ColumnInfo(name = "nome")
    String nome;
    @ColumnInfo(name = "valor")
    Double valor;
    @ColumnInfo(name = "pago")
    Boolean pago;
    @ColumnInfo(name = "categoria")
    Integer categoria;

    public Divida(String nome, Double valor, Boolean pago, Integer categoria) {
        this.nome = nome;
        this.valor = valor;
        this.pago = pago;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getValorFormatado() {
        return this.valor.toString();
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }
}
