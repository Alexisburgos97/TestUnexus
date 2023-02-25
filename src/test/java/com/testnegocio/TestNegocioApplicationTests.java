package com.testnegocio;

import com.testnegocio.entity.Cliente;
import com.testnegocio.entity.Sucursal;
import com.testnegocio.repository.ClienteRepository;
import com.testnegocio.repository.SucursalRepository;
import com.testnegocio.services.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TestNegocioApplicationTests {

    @Test
    void contextLoads() {
    }

}
