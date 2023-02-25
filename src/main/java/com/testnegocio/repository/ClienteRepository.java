package com.testnegocio.repository;

import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    @Query("select cl from Cliente cl where cl.nombres like %?1% or cl.identificacion like %?1%")
    public List<Cliente> findByNombresOrIdentificacion(String term);

    @Query("select cl from Cliente cl join fetch cl.sucursal su where cl.id=?1")
    public List<Cliente> findSucursalByClienteId(Long id);

}
