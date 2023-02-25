package com.testnegocio;

import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;

public class Datos {

    public final static Sucursal SUCURSAL = new Sucursal(null, "Test provincia", "Test ciudad", "Test direccion");
    public final static Sucursal SUCURSAL2 = new Sucursal(null, "Test provincia2", "Test ciudad2", "Test direccion2");

    public final static Cliente CLIENTE = new Cliente(null, "Test nombre", "testemail@gmail.com", "CEDULA", "123456789", "123123", new Sucursal(null,"Test provincia 22", "Test ciudad 22", "Test direccion 22"));
    public final static Cliente CLIENTE2 = new Cliente(null, "Test nombre2", "test2email@gmail.com", "CEDULA", "1234567891", "1231234",new Sucursal(null,"Test provincia 33", "Test ciudad 33", "Test direccion 33" ));

}
