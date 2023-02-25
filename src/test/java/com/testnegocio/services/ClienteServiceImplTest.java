package com.testnegocio.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testnegocio.Datos;
import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;
import com.testnegocio.repository.ClienteRepository;
import com.testnegocio.repository.SucursalRepository;
import com.testnegocio.services.impl.ClienteServiceImpl;
import com.testnegocio.services.impl.SucursalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    ClienteRepository clienteRepository;

    @InjectMocks
    ClienteServiceImpl service;

    @Mock
    SucursalRepository sucursalRepository;
    @InjectMocks
    SucursalServiceImpl sucursalService;

    Cliente cliente;
    Cliente cliente2;
    Cliente clienteGuardado;
    Cliente clienteGuardado2;

    ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
    }

    @Test
    void findAll() {
        // Given
        cliente = Datos.CLIENTE;
        cliente2 = Datos.CLIENTE2;

        clienteGuardado = clienteRepository.save(cliente);
        clienteGuardado2 = clienteRepository.save(cliente2);

        List<Cliente> listado = Arrays.asList(Datos.CLIENTE, Datos.CLIENTE2);
        when(clienteRepository.findAll()).thenReturn(listado);

        // when
        Iterable<Cliente> listadoClientes = service.findAll();

        // then
        assertFalse(listadoClientes.equals(""));
        assertEquals(2, StreamSupport.stream(listadoClientes.spliterator(), false).count());

        verify(clienteRepository).findAll();
    }

    @Test
    void findById() throws JsonProcessingException {
        // given
        Cliente cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "1122212331", "123123", new Sucursal(null,"Test provincia 22322", "Test ciudad 22232", "Test direccion 22322"));;
        when(clienteRepository.save(any())).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        Cliente clienteGuardado = service.save(cliente);

        when(clienteRepository.findById(clienteGuardado.getId())).thenReturn(Optional.of(clienteGuardado));

        Optional<Cliente> buscarCliente = service.findById(clienteGuardado.getId());

        // then
        assertTrue(buscarCliente.isPresent());
        assertEquals("Test nombre", buscarCliente.get().getNombres());

        verify(clienteRepository).save(any());
        verify(clienteRepository, times(1)).findById(any());
    }

    @Test
    void save() {
        // given
        Cliente cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "11222331", "123123", new Sucursal(null,"Test provincia 2232", "Test ciudad 2232", "Test direccion 2232"));;
        when(clienteRepository.save(any())).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        Cliente clienteGuardado = service.save(cliente);
        // then
        assertEquals("Test nombre", clienteGuardado.getNombres());
        assertEquals(3, clienteGuardado.getId());

        verify(clienteRepository).save(any());
    }

    @Test
    void deleteById() {
        // given
        Cliente cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "211222331", "123123", new Sucursal(null,"Test provincia 22322", "Test ciudad 22322", "Test direccion 22322"));;
        when(clienteRepository.save(any())).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        Cliente clienteGuardado = service.save(cliente);

        service.deleteById(clienteGuardado.getId());

        Optional<Cliente> buscarCliente = service.findById(clienteGuardado.getId());

        // then
        assertFalse(buscarCliente.isPresent());

        verify(clienteRepository).save(any());
    }

    @Test
    void findByNombresOrIdentificacion() {
        // given
        List<Cliente> clientesEncontrados = new ArrayList<Cliente>();

        Cliente cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "1122212331", "123123", new Sucursal(null,"Test provincia 22322", "Test ciudad 22232", "Test direccion 22322"));
        when(clienteRepository.save(any())).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        Cliente clienteGuardado = service.save(cliente);
        clientesEncontrados.add(clienteGuardado);

        when(clienteRepository.findByNombresOrIdentificacion(clienteGuardado.getNombres())).thenReturn(clientesEncontrados);

        clientesEncontrados = clienteRepository.findByNombresOrIdentificacion(clienteGuardado.getNombres());

        // then
        assertNotNull(clientesEncontrados);
        assertEquals(1, clientesEncontrados.size());
    }

    @Test
    void findSucursalByClienteId() {
        // given
        List<Sucursal> sucursalEncontrados = new ArrayList<Sucursal>();
        List<Cliente> sucursalEncontradosClientes = new ArrayList<Cliente>();

        Sucursal sucursal = new Sucursal(1L,"Test provincia 222322", "Test ciudad 222232", "Test direccion 222322");
        Sucursal sucursal2 = new Sucursal(2L,"Test provincia 2223223", "Test ciudad 2222323", "Test direccion 2223223");

        Cliente cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "1122212331", "123123", new Sucursal(null,"Test provincia 22322", "Test ciudad 22232", "Test direccion 22322"));;
        when(clienteRepository.save(any())).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        cliente.setSucursales(Arrays.asList(sucursal, sucursal2));

        // when
        Cliente clienteGuardado = service.save(cliente);
        Sucursal SucursalGuardado = sucursalService.save(sucursal);
        SucursalGuardado = sucursalService.save(sucursal2);

        sucursalEncontradosClientes.add(clienteGuardado);
        when(clienteRepository.findSucursalByClienteId(clienteGuardado.getId())).thenReturn(sucursalEncontradosClientes);

        sucursalEncontradosClientes = clienteRepository.findSucursalByClienteId(clienteGuardado.getId());

        sucursalEncontradosClientes.forEach( cli ->{
            sucursalEncontrados.add(cli.getSucursal());
            sucursalEncontrados.addAll(cli.getSucursales());
        });

        // then
        assertNotNull(sucursalEncontrados);
        assertEquals(3, sucursalEncontrados.size());
    }

}