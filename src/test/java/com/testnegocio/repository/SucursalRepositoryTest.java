package com.testnegocio.repository;

import com.testnegocio.Datos;
import com.testnegocio.entity.Sucursal;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SucursalRepositoryTest {

    @Autowired
    private SucursalRepository sucursalRepository;

    Sucursal sucursal;
    Sucursal sucursalGuardado;

    @BeforeEach
    void setUp() {
        sucursal = Datos.SUCURSAL;
        sucursalGuardado = sucursalRepository.save(sucursal);
    }

    @DisplayName("Test guardar una sucursal")
    @Test
    void testGuardarSucursal() {
        assertNotNull(sucursalGuardado);
        assertTrue(sucursalGuardado.getId() > 0, "");
    }

    @DisplayName("Test listar las sucursales")
    @Test
    void testListarSucursales(){
        Iterable<Sucursal> listaSucursales = sucursalRepository.findAll();

        assertNotNull(listaSucursales);
        assertEquals(1, StreamSupport.stream(listaSucursales.spliterator(), false).count());
    }

}