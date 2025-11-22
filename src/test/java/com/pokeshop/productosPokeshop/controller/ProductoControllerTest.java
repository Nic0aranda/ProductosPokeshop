package com.pokeshop.productosPokeshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokeshop.productosPokeshop.model.Categoria;
import com.pokeshop.productosPokeshop.model.Img;
import com.pokeshop.productosPokeshop.model.Producto;
import com.pokeshop.productosPokeshop.service.ImgService;
import com.pokeshop.productosPokeshop.service.ProductoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private ImgService imgService;

    private Producto sampleProducto() {
        Categoria cat = new Categoria();
        cat.setIdCategoria(1L);
        cat.setNombre("Booster Packs");

        Producto p = new Producto();
        p.setIdProducto(1L);
        p.setNombre("Booster Pack 1");
        p.setDescripcion("Booster Pack 1");
        p.setEdicion("std");
        p.setEstado("nuevo");
        p.setStock(20);
        p.setPrecio(19.99);
        p.setCategoria(cat);

        Img img = new Img();
        img.setIdImg(1L);
        img.setUrl("https://example.com/img/booster1.jpg");
        img.setProducto(p);

        p.setImagenes(List.of(img));
        return p;
    }

    @Test
    @DisplayName("GET /api/v1/productos - devuelve lista")
    void getAll_returnsList() throws Exception {
        Producto p = sampleProducto();
        when(productoService.findAll()).thenReturn(List.of(p));

        mvc.perform(get("/api/v1/productos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1))
                .andExpect(jsonPath("$[0].imagenes[0].idImg").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/productos/{id} - devuelve producto")
    void getById_returnsProduct() throws Exception {
        Producto p = sampleProducto();
        when(productoService.findById(1L)).thenReturn(Optional.of(p));

        mvc.perform(get("/api/v1/productos/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1))
                .andExpect(jsonPath("$.categoria.nombre").value("Booster Packs"));
    }

    @Test
    @DisplayName("POST /api/v1/productos - crea producto")
    void create_returnsCreated() throws Exception {
        Producto p = sampleProducto();
        Producto toCreate = new Producto();
        toCreate.setNombre("Nuevo");
        when(productoService.save(any())).thenReturn(p);

        mvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/productos/1"));
    }

    @Test
    @DisplayName("GET /api/v1/productos/{id}/imagenes - lista imágenes")
    void getImagesByProduct_returnsList() throws Exception {
        Producto p = sampleProducto();
        Img img = p.getImagenes().get(0);
        when(imgService.findByProductoId(1L)).thenReturn(List.of(img));

        mvc.perform(get("/api/v1/productos/1/imagenes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idImg").value(1))
                .andExpect(jsonPath("$[0].url").value("https://example.com/img/booster1.jpg"));
    }
}
