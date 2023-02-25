package com.testnegocio.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testnegocio.Datos;
import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;
import com.testnegocio.services.ClienteService;
import com.testnegocio.services.SucursalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NegocioController.class)
class NegocioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private SucursalService sucursalService;

    ObjectMapper objectMapper;

    Map<String, Object> response;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        response = new HashMap<>();
    }

    @DisplayName("Test listar clientes")
    @Test
    void listarClientes() throws Exception {
        // Given
        List<Cliente> clientes = Arrays.asList(Datos.CLIENTE, Datos.CLIENTE2);
        when(clienteService.findAll()).thenReturn(clientes);

        response.put("mensaje", "Clientes");
        response.put("status", true);
        response.put("data", clientes);

        // When
        mvc.perform(get("/api/clientes").contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.[0].nombres").value("Test nombre"))
                .andExpect(jsonPath("$.data.[1].nombres").value("Test nombre2"))
                .andExpect(jsonPath("$.data.[0].email").value("testemail@gmail.com"))
                .andExpect(jsonPath("$.data.[1].email").value("test2email@gmail.com"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(clienteService).findAll();
    }

    @DisplayName("Test buscar cliente por ID")
    @Test
    void testBuscarClienePorId() throws Exception {
        //given
        long clienteId = 1L;
        Cliente cliente = Datos.CLIENTE;

        when(clienteService.save(any())).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(clienteId);
            return c;
        });

        when(clienteService.findById(clienteId)).thenReturn(Optional.of(cliente));

        // when
        mvc.perform(get("/api/clientes/{id}", clienteId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(cliente.getId()))
                .andExpect(jsonPath("$.data.nombres").value(cliente.getNombres()))
                .andExpect(jsonPath("$.data.email").value(cliente.getEmail()))
                .andExpect(jsonPath("$.data.tipo_identificacion").value(cliente.getTipo_identificacion()))
                .andExpect(jsonPath("$.data.identificacion").value(cliente.getIdentificacion()))
                .andExpect(jsonPath("$.data.celular").value(cliente.getCelular()));

        verify(clienteService).findById(any());
    }

    @DisplayName("Test crear cliente")
    @Test
    void testCrearCliente() throws Exception {

        // Given
        Cliente cliente = Datos.CLIENTE;

        when(clienteService.save(any())).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        mvc.perform(post("/api/clientes").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(3)))
                .andExpect(jsonPath("$.data.nombres", is("Test nombre")))
                .andExpect(jsonPath("$.data.email", is("testemail@gmail.com")));
        verify(clienteService).save(any());
    }

    @DisplayName("Test editar cliente")
    @Test
    void testEditarCliente() throws Exception {

        // Given
        long clienteId = 3L;
        Cliente cliente = Datos.CLIENTE;
        Cliente cliente2 = Datos.CLIENTE2;

        when(clienteService.save(any(Cliente.class))).then(invocation -> {
            Cliente c = invocation.getArgument(0);
            c.setId(clienteId);
            return c;
        });

        when(clienteService.findById(clienteId)).thenReturn(Optional.of(cliente));

        when(clienteService.save(any(Cliente.class))).then((invocation) -> invocation.getArgument(0));

        // when
        mvc.perform(put("/api/clientes/{id}", clienteId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente2)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.nombres", is(cliente2.getNombres())))
                .andExpect(jsonPath("$.data.email", is(cliente2.getEmail())))
                .andExpect(jsonPath("$.data.tipo_identificacion", is(cliente2.getTipo_identificacion())));
        verify(clienteService).save(any());
    }

    @DisplayName("Test eliminar cliente")
    @Test
    void testEliminarCliente() throws Exception {

        // Given
        long clienteId = 1L;

        response.put("mensaje", "Cliente eliminado correctamente");
        response.put("status", true);

        // When
        mvc.perform(delete("/api/clientes/{id}", clienteId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("Cliente eliminado correctamente"))
                .andExpect(jsonPath("$.status").value((true)));

        verify(clienteService).deleteById(any());
    }

    @DisplayName("Test asignar sucursal al cliente")
    @Test
    void testAsignarSucursalPorCliente() throws Exception {

        // Given
        long clienteId = 1L;
        Cliente cliente = Datos.CLIENTE;

        List<Sucursal> sucursales = new ArrayList<Sucursal>(Arrays.asList(Datos.SUCURSAL, Datos.SUCURSAL2));
        cliente.setSucursales(sucursales);

        when(clienteService.save(any(Cliente.class))).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(clienteId);
            return c;
        });

        when(clienteService.findById(clienteId)).thenReturn(Optional.of(cliente));

        response.put("mensaje", "Sucursal asignada correctamente");
        response.put("status", true);
        response.put("data", cliente);

        // when
        mvc.perform(put("/api/clientes/{id}/asignar-sucursal", clienteId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursales)))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("Sucursal asignada correctamente"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.sucursales.length()").value(sucursales.size()));

        verify(clienteService).save(any());
        verify(clienteService).findById(any());
    }

    @DisplayName("Test eliminar una sucursal al cliente de sus sucursales")
    @Test
    void testEliminarSucursalPorCliente() throws Exception {
        // Given
        long clienteId = 1L;
        Cliente cliente = Datos.CLIENTE;
        Sucursal sucursal = Datos.SUCURSAL;

        List<Sucursal> sucursales = new ArrayList<Sucursal>(Arrays.asList(Datos.SUCURSAL, Datos.SUCURSAL2));
        cliente.setSucursales(sucursales);

        when(clienteService.save(any(Cliente.class))).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(clienteId);
            return c;
        });

        sucursales.remove(sucursal);
        cliente.setSucursales(sucursales);

        when(clienteService.findById(clienteId)).thenReturn(Optional.of(cliente));

        response.put("mensaje", "Sucursal eliminada correctamente");
        response.put("status", true);
        response.put("data", cliente);

        // when
        mvc.perform(put("/api/clientes/{id}/eliminar-sucursal", clienteId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.sucursales.length()").value(1));

        verify(clienteService).save(any());
        verify(clienteService).findById(any());
    }

    @DisplayName("Test filtrar por nombre o identificación del cliente")
    @Test
    void testFiltrarPorNombresOIdentificacion() throws Exception {

        //given
        String terminoABuscarNombre = "Test";

        List<Cliente> clientes = Arrays.asList(Datos.CLIENTE, Datos.CLIENTE2);

        when(clienteService.findByNombresOrIdentificacion(terminoABuscarNombre)).thenReturn(clientes);

        response.put("mensaje", "Clientes encontrados con el filtro: ".concat(terminoABuscarNombre));
        response.put("status", true);
        response.put("data", clientes);

        // When
        mvc.perform(get("/api/clientes/filtrar/{term}", terminoABuscarNombre).contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.[0].nombres").value("Test nombre"))
                .andExpect(jsonPath("$.data.[1].nombres").value("Test nombre2"))
                .andExpect(jsonPath("$.data.[0].email").value("testemail@gmail.com"))
                .andExpect(jsonPath("$.data.[1].email").value("test2email@gmail.com"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(clienteService).findByNombresOrIdentificacion(any());
    }

    @DisplayName("Test filtrar sucursales por cliente")
    @Test
    void testFiltrarSucursalesPorCliente() throws Exception {
        //given
        long clienteId = 1L;
        Cliente cliente = Datos.CLIENTE;

        List<Sucursal> listadoSucursales = new ArrayList<Sucursal>();

        List<Sucursal> sucursales = new ArrayList<Sucursal>(Arrays.asList(Datos.SUCURSAL, Datos.SUCURSAL2));
        cliente.setSucursales(sucursales);

        when(clienteService.save(any())).then(invocation ->{
            Cliente c = invocation.getArgument(0);
            c.setId(clienteId);
            return c;
        });

        when(clienteService.findById(clienteId)).thenReturn(Optional.of(cliente));

        listadoSucursales.add(cliente.getSucursal());
        listadoSucursales.addAll(cliente.getSucursales());

        response.put("mensaje", "Sucursales encontrados");
        response.put("status", true);
        response.put("data", listadoSucursales);

        // when
        mvc.perform(get("/api/clientes/{id}/sucursales", clienteId).contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("Sucursales encontrados"))
                .andExpect(jsonPath("$.status").value(true));
    }

    @DisplayName("Test listar sucursales")
    @Test
    void listarSucursales() throws Exception {

        // Given
        List<Sucursal> sucursales = Arrays.asList(Datos.SUCURSAL);
        when(sucursalService.findAll()).thenReturn(sucursales);

        response.put("mensaje", "Sucursales");
        response.put("status", true);
        response.put("data", sucursales);

        // When
        mvc.perform(get("/api/sucursales").contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.[0].provincia").value("Test provincia"))
                .andExpect(jsonPath("$.data.[0].ciudad").value("Test ciudad"))
                .andExpect(jsonPath("$.data.[0].direccion").value("Test direccion"))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(sucursalService).findAll();
    }

    @DisplayName("Test crear sucursal")
    @Test
    void testCrearSucursal() throws Exception {

        // Given
        Sucursal sucursal = Datos.SUCURSAL;

        when(sucursalService.save(any())).then(invocation ->{
            Sucursal s = invocation.getArgument(0);
            s.setId(2L);
            return s;
        });

        // when
        mvc.perform(post("/api/sucursales").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursal)))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(2)))
                .andExpect(jsonPath("$.data.provincia", is("Test provincia")))
                .andExpect(jsonPath("$.data.ciudad", is("Test ciudad")));
        verify(sucursalService).save(any());

    }

}