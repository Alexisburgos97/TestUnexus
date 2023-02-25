package com.testnegocio.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;
import com.testnegocio.services.ClienteService;
import com.testnegocio.services.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class NegocioController {

    @Autowired
    private ClienteService clientesService;

    @Autowired
    private SucursalService sucursalesService;

    ObjectMapper objectMapper = new ObjectMapper();

    //Clientes
    @GetMapping("/clientes")
    public ResponseEntity<?> listarClientes(){

        Iterable<Cliente> listado = null;
        Map<String, Object> response = new HashMap<>();

        try{

            listado = clientesService.findAll();

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Clientes");
        response.put("status", true);
        response.put("data", listado);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> buscarClienePorId(@PathVariable Long id){

        Optional<Cliente> opt = null;
        Map<String, Object> response = new HashMap<>();

        try{

            opt = clientesService.findById(id);

            if( !opt.isPresent() ){
                response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(", no existe"));
                response.put("status", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "El cliente se ha encontrado correctamente");
        response.put("status", true);
        response.put("data", opt.get());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/clientes")
    public ResponseEntity<?> crear(@RequestBody Cliente cliente){

        Cliente clienteDb = null;
        Sucursal sucursal = null;
        Optional <Sucursal> obtenerSucursal = null;
        Map<String, Object> response = new HashMap<>();

        try{

            sucursal = cliente.getSucursal();

            if( sucursal == null || "".equals(sucursal) ){
                response.put("mensaje", "Error, no existe sucursal");
                response.put("status", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            if( sucursal.getId() != null ){
                obtenerSucursal = sucursalesService.findById(sucursal.getId());
            }

            if( obtenerSucursal != null && obtenerSucursal.isPresent() ){
                cliente.setSucursal(obtenerSucursal.get());
            }else{
                cliente.setSucursal(sucursal);
            }

            clienteDb = clientesService.save(cliente);

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Cliente agregado correctamente");
        response.put("status", true);
        response.put("data", clienteDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> editar(@RequestBody Cliente cliente, @PathVariable Long id){

        Optional<Cliente> opt = null;
        Cliente clienteDb = null;
        Map<String, Object> response = new HashMap<>();

        try{

            opt = clientesService.findById(id);

            if( !opt.isPresent() ){
                response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(", no existe"));
                response.put("status", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            clienteDb = opt.get();

            clienteDb.setNombres(cliente.getNombres());
            clienteDb.setEmail(cliente.getEmail());
            clienteDb.setTipo_identificacion(cliente.getTipo_identificacion());
            clienteDb.setIdentificacion(cliente.getIdentificacion());
            clienteDb.setCelular(cliente.getCelular());
            //clienteDb.setSucursal(cliente.getSucursal());
            //clienteDb.setSucursales(cliente.getSucursales());

            clientesService.save(clienteDb);

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar el update en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Cliente actualizado correctamente");
        response.put("status", true);
        response.put("data", clienteDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){

        Map<String, Object> response = new HashMap<>();

        try{

            clientesService.deleteById(id);

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar el delete en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Cliente eliminado correctamente");
        response.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/clientes/{id}/asignar-sucursal")
    public ResponseEntity<?> asignarSucursalPorCliente(@RequestBody List<Sucursal> sucursales, @PathVariable Long id){

        Optional<Cliente> opt = null;
        Cliente clienteDb = null;
        Map<String, Object> response = new HashMap<>();

        try{

            opt = clientesService.findById(id);

            if( !opt.isPresent() ){
                response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(", no existe"));
                response.put("status", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            clienteDb = opt.get();
            Cliente finalClienteDb = clienteDb;

            Sucursal sucursalCliente = finalClienteDb.getSucursal();

            sucursales.forEach(su -> {
                finalClienteDb.addSucursal(su);
            });

            clientesService.save(finalClienteDb);

            response.put("data", finalClienteDb);

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Sucursal asignada correctamente");
        response.put("status", true);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/clientes/{id}/eliminar-sucursal")
    public ResponseEntity<?> eliminarSucursalPorCliente(@RequestBody Sucursal sucursal, @PathVariable Long id){

        Optional<Cliente> opt = null;
        Cliente clienteDb = null;
        Map<String, Object> response = new HashMap<>();

        try{

            opt = clientesService.findById(id);

            if( !opt.isPresent() ){
                response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(", no existe"));
                response.put("status", false);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            clienteDb = opt.get();
            clienteDb.removeSucursal(sucursal);

            clientesService.save(clienteDb);

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar el delete en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Sucursal eliminada correctamente");
        response.put("status", true);
        response.put("data", clienteDb);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/clientes/filtrar/{term}")
    public ResponseEntity<?> filtrarPorNombresOIdentificacion(@PathVariable String term){

        List<Cliente> listado = null;
        Map<String, Object> response = new HashMap<>();

        try{

            listado = clientesService.findByNombresOrIdentificacion(term);

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Clientes encontrados con el filtro: ".concat(term.toString()));
        response.put("status", true);
        response.put("data", listado);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/clientes/{id}/sucursales")
    public ResponseEntity<?> filtrarSucursalesPorCliente(@PathVariable Long id){

        List<Cliente> listado = null;
        List<Sucursal> listadoSucursales = new ArrayList<Sucursal>();
        Map<String, Object> response = new HashMap<>();

        try{

            listado = clientesService.findSucursalByClienteId(id);

            listado.forEach( lis -> {
                if( lis.getSucursal() != null ){
                    listadoSucursales.add(lis.getSucursal());
                }
                if( lis.getSucursales() != null ){
                    listadoSucursales.addAll(lis.getSucursales());
                }
            });

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Sucursales encontrados");
        response.put("status", true);
        response.put("data", listadoSucursales);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    //Sucursales
    @GetMapping("/sucursales")
    public ResponseEntity<?> listarSucursales(){
        Iterable<Sucursal> listado = null;
        Map<String, Object> response = new HashMap<>();

        try{

            listado = sucursalesService.findAll();

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Sucursales");
        response.put("status", true);
        response.put("data", listado);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/sucursales")
    public ResponseEntity<?> crearSucursal(@RequestBody Sucursal sucursal){

        Sucursal sucursalDb = null;
        Map<String, Object> response = new HashMap<>();

        try{

            sucursalDb = sucursalesService.save(sucursal);

        }catch (DataAccessException e){
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("status", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("mensaje", "Sucursales");
        response.put("status", true);
        response.put("data", sucursalDb);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
