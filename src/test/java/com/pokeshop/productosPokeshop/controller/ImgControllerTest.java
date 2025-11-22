package com.pokeshop.productosPokeshop.controller;

import com.pokeshop.productosPokeshop.service.ImgService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImgController.class)
class ImgControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImgService imgService;

    @Test
    @DisplayName("DELETE /api/v1/imagenes/{id} - borra imagen")
    void deleteImage_returnsNoContent() throws Exception {
        mvc.perform(delete("/api/v1/imagenes/1"))
                .andExpect(status().isNoContent());
        verify(imgService).deleteImage(1L);
    }
}
