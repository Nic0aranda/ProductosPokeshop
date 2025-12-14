package com.pokeshop.productosPokeshop.init;

import com.pokeshop.productosPokeshop.model.Categoria;
import com.pokeshop.productosPokeshop.model.Producto;
import com.pokeshop.productosPokeshop.repository.CategoriaRepository;
import com.pokeshop.productosPokeshop.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DataInitializer(CategoriaRepository categoriaRepository, 
                          ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verificar si ya hay datos
        if (categoriaRepository.count() == 0 && productoRepository.count() == 0) {
            System.out.println("=== INICIALIZANDO DATOS DE PRUEBA ===");
            
            // Crear y guardar categorías
            Categoria cat1 = categoriaRepository.save(
                Categoria.builder()
                    .nombre("Booster Packs")
                    .build()
            );
            
            Categoria cat2 = categoriaRepository.save(
                Categoria.builder()
                    .nombre("Sobres")
                    .build()
            );
            
            Categoria cat3 = categoriaRepository.save(
                Categoria.builder()
                    .nombre("Cartas Sueltas")
                    .build()
            );
            
            // Crear productos
            List<Producto> productos = Arrays.asList(
                Producto.builder()
                    .nombre("Booster Pack 1")
                    .descripcion("Booster Pack 1")
                    .edicion("std")
                    .estado("nuevo")
                    .stock(10)
                    .precio(19.99)
                    .categoria(cat1)
                    .build(),
                    
                Producto.builder()
                    .nombre("Booster Pack 2")
                    .descripcion("Booster Pack 2")
                    .edicion("std")
                    .estado("nuevo")
                    .stock(25)
                    .precio(24.99)
                    .categoria(cat1)
                    .build(),
                    
                Producto.builder()
                    .nombre("Booster Pack 3")
                    .descripcion("Booster Pack 3")
                    .edicion("std")
                    .estado("nuevo")
                    .stock(50)
                    .precio(29.99)
                    .categoria(cat1)
                    .build(),
                    
                Producto.builder()
                    .nombre("Sobre 1")
                    .descripcion("Sobre 1")
                    .edicion("std")
                    .estado("nuevo")
                    .stock(15)
                    .precio(14.99)
                    .categoria(cat2)
                    .build(),
                    
                Producto.builder()
                    .nombre("Sobre 2")
                    .descripcion("Sobre 2")
                    .edicion("std")
                    .estado("nuevo")
                    .stock(30)
                    .precio(19.99)
                    .categoria(cat2)
                    .build(),
                    
                Producto.builder()
                    .nombre("Sobre 3")
                    .descripcion("Sobre 3")
                    .edicion("std")
                    .estado("nuevo")
                    .stock(70)
                    .precio(19.99)
                    .categoria(cat2)
                    .build(),
                    
                Producto.builder()
                    .nombre("Carta Solitaria 1")
                    .descripcion("Carta Solitaria 1")
                    .edicion("std")
                    .estado("nuevo")
                    .stock(5)
                    .precio(4.99)
                    .categoria(cat3)
                    .build(),
                    
                Producto.builder()
                    .nombre("Pikachu Illustrator")
                    .descripcion("Pikachu Illustrator")
                    .edicion("coleccionable")
                    .estado("nuevo")
                    .stock(1)
                    .precio(999999.99)
                    .categoria(cat3)
                    .build()
            );
            
            // Guardar todos los productos
            productoRepository.saveAll(productos);
            
            System.out.println("=== DATOS DE PRUEBA INICIALIZADOS ===");
            System.out.println("Categorías creadas: 3");
            System.out.println("Productos creados: 8");
        }
    }
}