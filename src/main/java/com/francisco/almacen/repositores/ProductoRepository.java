package com.francisco.almacen.repositores;

import com.francisco.almacen.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductoRepository extends JpaRepository<Producto, Long>{
}
