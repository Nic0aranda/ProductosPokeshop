package com.pokeshop.productosPokeshop.service;

import com.pokeshop.productosPokeshop.dto.ImagesJsonRequest;
import com.pokeshop.productosPokeshop.model.Img;
import com.pokeshop.productosPokeshop.model.Producto;
import com.pokeshop.productosPokeshop.repository.ImgRepository;
import com.pokeshop.productosPokeshop.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.nio.file.*;
import java.util.*;
import java.util.Base64.Decoder;

@Service
public class ImgService {

    private final ProductoRepository productoRepository;
    private final ImgRepository imgRepository;
    private final Path uploadDir;

    public ImgService(ProductoRepository productoRepository,
                      ImgRepository imgRepository,
                      @Value("${file.upload-dir:uploads}") String uploadDirProperty) {
        this.productoRepository = productoRepository;
        this.imgRepository = imgRepository;
        this.uploadDir = Paths.get(uploadDirProperty);
        try { Files.createDirectories(this.uploadDir); } catch (Exception ignored) {}
    }

    @Transactional
    public List<Img> saveImagesFromBase64(Long productoId, List<ImagesJsonRequest.ImageItem> images) throws Exception {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoId));

        List<Img> saved = new ArrayList<>();
        Decoder decoder = Base64.getDecoder();

        for (var item : images) {
            if (item == null || item.base64 == null || item.base64.isBlank()) continue;

            String base64data = item.base64.trim();
            String mime = null;

            if (base64data.startsWith("data:")) {
                int comma = base64data.indexOf(',');
                if (comma > 0) {
                    String meta = base64data.substring(5, comma);
                    String[] parts = meta.split(";");
                    mime = parts[0];
                    base64data = base64data.substring(comma + 1);
                }
            }

            byte[] bytes;
            try {
                bytes = decoder.decode(base64data);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Base64 inválido para imagen: " + (item.filename != null ? item.filename : ""));
            }

            String ext = "bin";
            if (mime != null) {
                if (mime.equals("image/png")) ext = "png";
                else if (mime.equals("image/jpeg") || mime.equals("image/jpg")) ext = "jpg";
                else if (mime.equals("image/gif")) ext = "gif";
            } else {
                if (bytes.length > 4) {
                    if (bytes[0]==(byte)0x89 && bytes[1]==(byte)0x50) ext="png";
                    else if (bytes[0]==(byte)0xFF && bytes[1]==(byte)0xD8) ext="jpg";
                }
            }

            String baseName = (item.filename != null && !item.filename.isBlank())
                    ? item.filename.replaceAll("[^a-zA-Z0-9-_\\.]", "_")
                    : UUID.randomUUID().toString();
            String filename = baseName + "_" + System.currentTimeMillis() + "." + ext;
            Path target = uploadDir.resolve(filename);

            try (OutputStream os = Files.newOutputStream(target, StandardOpenOption.CREATE_NEW)) {
                os.write(bytes);
            }

            String publicUrl = "/uploads/" + filename;

            Img img = new Img();
            img.setUrl(publicUrl);
            img.setProducto(producto);
            saved.add(imgRepository.save(img));
        }

        return saved;
    }

    // Reemplaza la implementación anterior por esta llamada directa al repositorio
    public List<Img> findByProductoId(Long productoId) {
        return imgRepository.findByProductoIdFetch(productoId);
    }

    @Transactional
    public void deleteImage(Long id) {
        imgRepository.findById(id).ifPresent(img -> {
            try {
                String url = img.getUrl();
                if (url != null && url.startsWith("/uploads/")) {
                    Path file = uploadDir.resolve(url.substring("/uploads/".length()));
                    try { Files.deleteIfExists(file); } catch (Exception ignored) {}
                }
            } finally {
                imgRepository.delete(img);
            }
        });
    }
}