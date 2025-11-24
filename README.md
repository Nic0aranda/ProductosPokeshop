Iniciar el xampp Inicializar el apache 
Inicializar Mysql en el puerto "3306" 
Tener un usuario llamado "root" 
El usuario al menos en la configuracion por defecto no tiene contraseña (Este usuario deberia existir por defecto en nuestra bd)

Redirigirse con el cmd a la ubicacion de la carpeta.

Ejecutar este comando con el cmd: .\mvnw.cmd -DskipTests=true spring-boot:run

Para visualizar swagger ir a http://localhost:8081/swagger-ui/index.html

Con esto el microservicio deberia de ejecutarse

comando para realizar las pruebas: .\mvnw.cmd test

Datos de ejemplo en BD
INSERT INTO `producto`(`id_producto`, `descripcion`, `edicion`, `estado`, `nombre`, `stock`, `id_categoria`, `precio`) VALUES 
('1', 'Booster Pack 1', 'std', 'nuevo', 'Booster Pack 1', 10, 1, 19.99),
('2', 'Booster Pack 2', 'std', 'nuevo', 'Booster Pack 2', 25, 1, 24.99),
('3', 'Booster Pack 3', 'std', 'nuevo', 'Booster Pack 3', 30, 1, 29.99),
('4', 'Sobre 1', 'std', 'nuevo', 'Sobre 1', 10, 2, 9.99),
('5', 'Sobre 2', 'std', 'nuevo', 'Sobre 2', 50, 2, 14.99),
('6', 'Sobre 3', 'std', 'nuevo', 'Sobre 3', 70, 2, 19.99),
('7', 'Carta Solitaria 1', 'std', 'nuevo', 'Carta Solitaria 1', 5, 3, 4.99),
('8', 'Pikachu Illustrator rare', 'coleccionable', 'nuevo', 'Pikachu Illustrator', 1, 3, 999999.99);
