package com.testnegocio.repository;

import com.testnegocio.entity.Sucursal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface SucursalRepository extends CrudRepository<Sucursal, Long> {



}
