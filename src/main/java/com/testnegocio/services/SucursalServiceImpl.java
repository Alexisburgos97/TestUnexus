package com.testnegocio.services;

import com.testnegocio.entity.Sucursal;
import com.testnegocio.repository.SucursalRepository;
import com.testnegocio.services.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SucursalServiceImpl implements SucursalService {

    @Autowired
    private SucursalRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Sucursal> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sucursal> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Sucursal save(Sucursal sucusal) {
        return repository.save(sucusal);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
