package com.testnegocio.entity;

import jakarta.persistence.*;

@Entity
@Table(name="sucursales")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long id;

    @Column(name = "provincia", nullable = false)
    private String provincia;

    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @OneToOne(mappedBy = "sucursal")
    private Cliente cliente;

    public Sucursal(){

    }

    public Sucursal(Long id, String provincia, String ciudad, String direccion) {
        this.id = id;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object obj) {

        if( this == obj ){
            return true;
        }

        if( !(obj instanceof Sucursal) ){
            return false;
        }

        Sucursal su = (Sucursal) obj;

        return this.id != null && this.id.equals(su.getId());
    }

}
