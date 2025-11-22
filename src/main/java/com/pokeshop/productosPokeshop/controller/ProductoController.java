package com.pokeshop.productosPokeshop.controller;

import com.pokeshop.productosPokeshop.model.Img;
import com.pokeshop.productosPokeshop.model.Producto;
import com.pokeshop.productosPokeshop.service.ImgService;
import com.pokeshop.productosPokeshop.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ImgService imgService;

    public ProductoController(ProductoService productoService, ImgService imgService) {
        this.productoService = productoService;
        this.imgService = imgService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAll(@RequestParam(required = false) String categoria) {
        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(productoService.findByCategoriaNombre(categoria));
        }
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        Producto saved = productoService.save(producto);
        return ResponseEntity.created(URI.create("/api/v1/productos/" + saved.getIdProducto())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setIdProducto(id);
        Producto updated = productoService.save(producto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener imágenes asociadas a un producto
    @GetMapping("/{id}/imagenes")
    public ResponseEntity<List<Img>> getImagenesByProducto(@PathVariable Long id) {
        return ResponseEntity.ok(imgService.findByProductoId(id));
    }

    // Recibir imágenes en JSON (base64) y asociarlas al producto
    @PostMapping("/{id}/imagenes-json")
    public ResponseEntity<List<Img>> uploadImagesJson(@PathVariable Long id, @RequestBody com.pokeshop.productosPokeshop.dto.ImagesJsonRequest request) throws Exception {
        var saved = imgService.saveImagesFromBase64(id, request.images);
        return ResponseEntity.ok(saved);
    }
}