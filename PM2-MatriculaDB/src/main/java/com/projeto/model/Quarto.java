package com.projeto.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // Tipo do quarto (ex: Solteiro, Duplo, Suíte)

    private Double preco; // Preço por diária

    @ManyToMany
    private Set<Cliente> clientes = new HashSet<>();

    public Quarto() {}

    public Quarto(String tipo, Double preco) {
        this.tipo = tipo;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Double getPreco() {
        return preco;
    }

    public Set<Cliente> getClientes() {
        return clientes;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public void setClientes(Set<Cliente> clientes) {
        this.clientes = clientes;
    }
}