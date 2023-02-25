package com.testnegocio.repository;

import com.testnegocio.Datos;
import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClienteRepositoryTest {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    Cliente cliente;
    Cliente cliente2;
    Cliente clienteGuardado;
    Cliente clienteGuardado2;
    Sucursal sucursal;
    Sucursal sucursalGuardado;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("Test de listado de clientes")
    @Test
    void testListarClientes() {
        cliente = Datos.CLIENTE;
        cliente2 = Datos.CLIENTE2;

        clienteGuardado = clienteRepository.save(cliente);
        clienteGuardado2 = clienteRepository.save(cliente2);

        Iterable<Cliente> listadoClientes = clienteRepository.findAll();

        assertNotNull(listadoClientes);
        assertEquals(2, StreamSupport.stream(listadoClientes.spliterator(), false).count());
    }

    @DisplayName("Test buscar un cliente por su ID")
    @Test
    void testBuscarPorId() {
        cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "1231", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));;
        clienteGuardado = clienteRepository.save(cliente);

        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteGuardado.getId());

        assertNotNull(clienteOptional);
        assertTrue(clienteOptional.isPresent());
        assertEquals("Test nombre", clienteOptional.orElseThrow().getNombres());
        assertTrue(clienteOptional.get().getId() > 0);
    }

    @DisplayName("Test guardar cliente con una sucursal")
    @Test
    void testGuardarCliente() {
        cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "12331", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));;
        clienteGuardado = clienteRepository.save(cliente);

        assertNotNull(clienteGuardado);
        assertNotNull(clienteGuardado.getSucursal());
        assertTrue(clienteGuardado.getId() > 0);
    }

    @DisplayName("Test guardar cliente con una sucursal y varias sucursales")
    @Test
    void testGuardarClienteConSucursales() {
        cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "122331", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));;
        clienteGuardado = clienteRepository.save(cliente);

        sucursal = new Sucursal(null,"Test provincia 222", "Test ciudad 222", "Test direccion 222");
        sucursalGuardado = sucursalRepository.save(sucursal);

        List<Sucursal> listadoSucursales = new ArrayList<Sucursal>();

        for(int i = 0; i < 5; i++){
            sucursal = new Sucursal();
            sucursal.setProvincia("Buenos Aires " + i);
            sucursal.setCiudad("Ciudad de buenos aires " + i);
            sucursal.setDireccion("Avenida Gaona 1000 " + i);

            sucursalGuardado = sucursalRepository.save(sucursal);
            listadoSucursales.add(sucursalGuardado);
        }

        cliente.setSucursales(listadoSucursales);

        clienteGuardado = clienteRepository.save(cliente);

        assertNotNull(clienteGuardado);
        assertNotNull(clienteGuardado.getSucursal());
        assertEquals(5, clienteGuardado.getSucursales().size());
        assertTrue(clienteGuardado.getId() > 0);
    }

    @DisplayName("Test actualizar un cliente")
    @Test
    void testActualizarCliente() {
        cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "11222331", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));;
        clienteGuardado = clienteRepository.save(cliente);

        clienteGuardado.setNombres("Test actualizado");
        clienteGuardado.setEmail("testActualizado@gmail.com");
        clienteGuardado.setTipo_identificacion("RUC");
        clienteGuardado.setIdentificacion("0266537341001");
        clienteGuardado.setCelular("2222222");
        clienteGuardado.setSucursal(sucursalGuardado);

        Cliente clienteActualizado = clienteRepository.save(clienteGuardado);

        assertEquals("Test actualizado", clienteActualizado.getNombres());
        assertEquals("testActualizado@gmail.com", clienteActualizado.getEmail());
        assertEquals("RUC", clienteActualizado.getTipo_identificacion());
        assertEquals("0266537341001", clienteActualizado.getIdentificacion());
        assertEquals("2222222", clienteActualizado.getCelular());
        assertEquals(sucursalGuardado, clienteActualizado.getSucursal());
    }

    @DisplayName("Test eliminar un cliente")
    @Test
    void testEliminarCliente() {
        cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "112", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));;
        clienteGuardado = clienteRepository.save(cliente);

        clienteRepository.deleteById(clienteGuardado.getId());

        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteGuardado.getId());

        assertFalse(clienteOptional.isPresent());
    }

    @DisplayName("Test buscar cliente por nombre o identificación")
    @Test
    void testFindByNombresOrIdentificacion() {
        cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "1232112", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));;
        clienteGuardado = clienteRepository.save(cliente);

        List<Cliente> clientesEncontrados = clienteRepository.findByNombresOrIdentificacion(clienteGuardado.getIdentificacion());
        /*List<Cliente> clientesEncontrados = clienteRepository.findByNombresOrIdentificacion(clienteGuardado.getNombres());*/

        assertNotNull(clientesEncontrados);
        assertEquals(1, clientesEncontrados.size());
    }

    @DisplayName("Test buscar sucursales asignados al cliente")
    @Test
    void testFindSucursalByClienteId() {
        cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "3221", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));;
        clienteGuardado = clienteRepository.save(cliente);

        sucursal = new Sucursal(null,"Test provincia 232", "Test ciudad 232", "Test direccion 232");
        sucursalGuardado = sucursalRepository.save(sucursal);

        List<Sucursal> listadoSucursales = new ArrayList<Sucursal>();
        listadoSucursales.add(sucursalGuardado);

        for(int i = 0; i < 5; i++){
            sucursal = new Sucursal();
            sucursal.setProvincia("Buenos Aires " + i);
            sucursal.setCiudad("Ciudad de buenos aires " + i);
            sucursal.setDireccion("Avenida Gaona 1000 " + i);

            sucursalGuardado = sucursalRepository.save(sucursal);
            listadoSucursales.add(sucursalGuardado);
        }
        cliente.setSucursales(listadoSucursales);

        clienteGuardado = clienteRepository.save(cliente);

        List<Cliente> listadoSucursalesCliente = clienteRepository.findSucursalByClienteId(clienteGuardado.getId());

        assertNotNull(listadoSucursalesCliente);
        assertEquals(6, clienteGuardado.getSucursales().size());
        assertTrue(clienteGuardado.getId() > 0);
    }

    @DisplayName("Test eliminar sucursal a un cliente en las sucursales que tenga")
    @Test
    void testEliminarSucursalACliente() {
        cliente = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "322221", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));;
        clienteGuardado = clienteRepository.save(cliente);

        sucursal = new Sucursal(null,"Test provincia 2232", "Test ciudad 2232", "Test direccion 2232");
        sucursalGuardado = sucursalRepository.save(sucursal);

        List<Sucursal> listadoSucursales = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            sucursal = new Sucursal();
            sucursal.setProvincia("Buenos Aires " + i);
            sucursal.setCiudad("Ciudad de buenos aires " + i);
            sucursal.setDireccion("Avenida Gaona 1000 " + i);

            sucursalGuardado = sucursalRepository.save(sucursal);
            listadoSucursales.add(sucursalGuardado);
        }
        cliente.setSucursales(listadoSucursales);

        clienteGuardado = clienteRepository.save(cliente);

        List<Cliente> listadoSucursalesCliente = clienteRepository.findSucursalByClienteId(clienteGuardado.getId());

        assertNotNull(listadoSucursales);
        assertEquals(5, clienteGuardado.getSucursales().size());
        assertTrue(clienteGuardado.getId() > 0);


        //Elimino la última sucursal creada en el for
        listadoSucursales.remove(sucursalGuardado);
        cliente.setSucursales(listadoSucursales);

        Cliente clienteGuardado2 = clienteRepository.save(cliente);

        assertNotNull(listadoSucursales);
        assertEquals(4, clienteGuardado2.getSucursales().size());
        assertTrue(clienteGuardado.getId() > 0);
    }

}