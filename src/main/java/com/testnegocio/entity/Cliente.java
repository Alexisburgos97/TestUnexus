package com.testnegocio.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "tipo_identificacion", nullable = false)
    private String tipo_identificacion;

    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @Column(name = "celular")
    private String celular;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="fk_id_sucursal")
    private Sucursal sucursal;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Sucursal> sucursales;

    public Cliente(){
        this.sucursales = new ArrayList<>();
    }


    public Cliente(Long id, String nombres, String email, String tipo_identificacion, String identificacion, String celular) {
        this.id = id;
        this.nombres = nombres;
        this.email = email;
        this.tipo_identificacion = tipo_identificacion;
        this.identificacion = identificacion;
        this.celular = celular;
        this.sucursales = new ArrayList<>();
    }

    public Cliente(Long id, String nombres, String email, String tipo_identificacion, String identificacion, String celular, Sucursal sucursal) {
        this.id = id;
        this.nombres = nombres;
        this.email = email;
        this.tipo_identificacion = tipo_identificacion;
        this.identificacion = identificacion;
        this.celular = celular;
        this.sucursal = sucursal;
        this.sucursales = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo_identificacion() {
        return tipo_identificacion;
    }

    public void setTipo_identificacion(String tipo_identificacion) {
        this.tipo_identificacion = tipo_identificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }

    public void addSucursal(Sucursal sucursal){
        this.sucursales.add(sucursal);
    }

    public void removeSucursal(Sucursal sucursal){
        this.sucursales.remove(sucursal);
    }
}
