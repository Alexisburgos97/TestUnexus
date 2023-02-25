package com.testnegocio.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testnegocio.Datos;
import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;
import com.testnegocio.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalServiceImplTest {

    @Mock
    SucursalRepository sucursalRepository;
    @InjectMocks
    SucursalServiceImpl sucursalService;

    Sucursal sucursal;
    Sucursal sucursal2;
    Sucursal sucursalGuardado;
    Sucursal sucursalGuardado2;

    ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
    }

    @Test
    void findAll() {
        // Given
        sucursal = Datos.SUCURSAL;
        sucursal2 = Datos.SUCURSAL2;

        sucursalGuardado = sucursalRepository.save(sucursal);
        sucursalGuardado2 = sucursalRepository.save(sucursal2);

        List<Sucursal> listado = Arrays.asList(Datos.SUCURSAL, Datos.SUCURSAL2);
        when(sucursalRepository.findAll()).thenReturn(listado);

        // when
        Iterable<Sucursal> listadoSucursales = sucursalService.findAll();

        // then
        assertFalse(listadoSucursales.equals(""));
        assertEquals(2, StreamSupport.stream(listadoSucursales.spliterator(), false).count());

        verify(sucursalRepository).findAll();
    }

    @Test
    void findById() {

    }

    @Test
    void save() {
        // given
        Sucursal sucursal = new Sucursal(null,"Test provincia 22232", "Test ciudad 22322", "Test direccion 22232");
        when(sucursalRepository.save(any())).then(invocation ->{
            Sucursal su = invocation.getArgument(0);
            su.setId(3L);
            return su;
        });

        // when
        Sucursal sucursalGuardado = sucursalService.save(sucursal);

        // then
        assertEquals("Test provincia 22232", sucursalGuardado.getProvincia());
        assertEquals(3, sucursalGuardado.getId());

        verify(sucursalRepository).save(any());
    }

    @Test
    void deleteById() {

    }
}