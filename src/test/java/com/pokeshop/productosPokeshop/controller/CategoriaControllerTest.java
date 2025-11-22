package com.pokeshop.productosPokeshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokeshop.productosPokeshop.model.Categoria;
import com.pokeshop.productosPokeshop.service.CategoriaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CategoriaService categoriaService;

    @Test
    @DisplayName("GET /api/v1/categorias - devuelve todas")
    void getAll_returnsList() throws Exception {
        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setNombre("Booster Packs");
        when(categoriaService.findAll()).thenReturn(List.of(c));

        mvc.perform(get("/api/v1/categorias").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCategoria").value(1));
    }

    @Test
    @DisplayName("GET /api/v1/categorias/{id} - devuelve 200")
    void getById_returnsCategory() throws Exception {
        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setNombre("Booster Packs");
        when(categoriaService.findById(1L)).thenReturn(Optional.of(c));

        mvc.perform(get("/api/v1/categorias/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Booster Packs"));
    }

    @Test
    @DisplayName("POST /api/v1/categorias - crea categoría")
    void create_returnsCreated() throws Exception {
        Categoria c = new Categoria();
        c.setIdCategoria(2L);
        c.setNombre("Sobres");
        when(categoriaService.save(any())).thenReturn(c);

        Categoria payload = new Categoria();
        payload.setNombre("Sobres");

        mvc.perform(post("/api/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/categorias/2"));
    }

    @Test
    @DisplayName("DELETE /api/v1/categorias/{id} - borra")
    void delete_returnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/categorias/1"))
                .andExpect(status().isNoContent());
        verify(categoriaService).deleteById(1L);
    }
}