package com.aprendamos.rest.repositorio;

import com.aprendamos.rest.entidades.producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface repositorioProducto extends JpaRepository<producto, Integer> {
        List<producto> findByNombreContaining(String nombre);
}
