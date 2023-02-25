package com.testnegocio.services;

import com.testnegocio.entity.Sucursal;

import java.util.Optional;

public interface SucursalService {

    public Iterable<Sucursal> findAll();

    public Optional<Sucursal> findById(Long id);

    public Sucursal save( Sucursal sucursal );

    public void deleteById(Long id);

}
