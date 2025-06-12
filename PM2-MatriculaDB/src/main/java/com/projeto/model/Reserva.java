package com.projeto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente; // Relacionamento com o cliente

    @ManyToOne
    private Quarto quarto; // Relacionamento com o quarto

    private LocalDateTime dataCheckIn; // Data de check-in
    private LocalDateTime dataCheckOut; // Data de check-out

    public Reserva() {}

    public Reserva(Cliente cliente, Quarto quarto, LocalDateTime dataCheckIn, LocalDateTime dataCheckOut) {
        this.cliente = cliente;
        this.quarto = quarto;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public LocalDateTime getDataCheckIn() {
        return dataCheckIn;
    }

    public LocalDateTime getDataCheckOut() {
        return dataCheckOut;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public void setDataCheckIn(LocalDateTime dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public void setDataCheckOut(LocalDateTime dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }
}