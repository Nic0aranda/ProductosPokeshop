package com.pokeshop.productosPokeshop.service;

import com.pokeshop.productosPokeshop.model.Img;
import com.pokeshop.productosPokeshop.model.Producto;
import com.pokeshop.productosPokeshop.repository.CategoriaRepository;
import com.pokeshop.productosPokeshop.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // usar fetch para evitar problemas de lazy / serialización
    public List<Producto> findAll() {
        return productoRepository.findAllWithImages();
    }

    // usar la query con join para devolver imágenes ya cargadas
    public List<Producto> findByCategoriaNombre(String nombreCategoria) {
        return productoRepository.findByCategoriaNombreFetch(nombreCategoria);
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto save(Producto producto) {
        // Si envían solo { categoria: { idCategoria: X } } -> obtener la entidad gestionada
        if (producto.getCategoria() != null && producto.getCategoria().getIdCategoria() != null) {
            categoriaRepository.findById(producto.getCategoria().getIdCategoria())
                    .ifPresent(producto::setCategoria);
        }

        // Asegurar que cada img referencie al producto (para Cascade persist)
        if (producto.getImagenes() != null) {
            for (Img img : producto.getImagenes()) {
                img.setProducto(producto);
            }
        }
        return productoRepository.save(producto);
    }

    @Transactional
    public void descontarStock(Long idProducto, int cantidadArestar) {
        // 1. Buscar el producto
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProducto));

        // 2. Verificar que haya suficiente stock
        if (producto.getStock() < cantidadArestar) {
            throw new RuntimeException("Stock insuficiente. Solicitado: " + cantidadArestar + ", Disponible: " + producto.getStock());
        }

        // 3. Restar y guardar
        int nuevoStock = producto.getStock() - cantidadArestar;
        producto.setStock(nuevoStock);
        
        productoRepository.save(producto);
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }
}