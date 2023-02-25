package com.testnegocio.services;

import com.testnegocio.Datos;
import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;
import com.testnegocio.repository.ClienteRepository;
import com.testnegocio.repository.SucursalRepository;
import com.testnegocio.services.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private ClienteRepository repository;
    private SucursalRepository sucursalRepository;

    public ClienteServiceImpl(ClienteRepository repository, SucursalRepository sucursalRepository) {
        this.repository = repository;
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Cliente> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Cliente> findByNombresOrIdentificacion(String term){
        return repository.findByNombresOrIdentificacion(term);
    }

    @Override
    public List<Cliente> findSucursalByClienteId(Long id) {
        return repository.findSucursalByClienteId(id);
    }

}
