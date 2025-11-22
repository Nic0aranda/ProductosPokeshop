package com.pokeshop.productosPokeshop.repository;

import com.pokeshop.productosPokeshop.model.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ImgRepository extends JpaRepository<Img, Long> {

    // carga la imagen con su producto y categoria para evitar LazyInitializationException al serializar
    @Query("select i from Img i join fetch i.producto p join fetch p.categoria c where p.idProducto = :productoId")
    List<Img> findByProductoIdFetch(@Param("productoId") Long productoId);

    // mantenemos el método simple por si se usa en otros lugares
    List<Img> findByProducto_IdProducto(Long productoId);

}
