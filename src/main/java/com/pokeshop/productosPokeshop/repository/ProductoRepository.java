package com.pokeshop.productosPokeshop.repository;

import com.pokeshop.productosPokeshop.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("select distinct p from Producto p join p.categoria c left join fetch p.imagenes i where c.nombre = :nombre")
    List<Producto> findByCategoriaNombreFetch(@Param("nombre") String nombreCategoria);

    @Query("select distinct p from Producto p left join fetch p.imagenes i left join fetch p.categoria c")
    List<Producto> findAllWithImages();
}