package com.pokeshop.productosPokeshop.dto;

import java.util.List;

public class ImagesJsonRequest {
    public static class ImageItem {
        public String base64;    // "data:image/png;base64,...." o solo base64
        public String filename;  // opcional
    }
    public List<ImageItem> images;
}
