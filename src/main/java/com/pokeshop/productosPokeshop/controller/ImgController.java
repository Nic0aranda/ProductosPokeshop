package com.pokeshop.productosPokeshop.controller;

import com.pokeshop.productosPokeshop.service.ImgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/imagenes")
public class ImgController {

    private final ImgService imgService;

    public ImgController(ImgService imgService) {
        this.imgService = imgService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imgService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
