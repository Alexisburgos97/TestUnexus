package com.testnegocio.services;

import com.testnegocio.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    public Iterable<Cliente> findAll();

    public Optional<Cliente> findById(Long id);

    public Cliente save( Cliente cliente );

    public void deleteById(Long id);

    public List<Cliente> findByNombresOrIdentificacion(String term);

    public List<Cliente> findSucursalByClienteId(Long id);

}
