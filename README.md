# Foro Alura - API REST

API desarrollada con **Spring Boot** para gestionar tópicos de un foro, permitiendo crear, listar, actualizar y eliminar tópicos.

---

## **Configuración del proyecto**

- Java 21+
- Spring Boot 4.0.3
- Maven
- Dependencias:
  - Spring Web
  - Spring Data JPA
  - Spring Security
  - MySQL Driver
  - Lombok
  - Validation
  - Flyway Migration
  - Spring Boot DevTools

- Base de datos: MySQL (`forohub`)
- Credenciales de Spring Security:
  - Usuario: `emmanuel`
  - Contraseña: `1234`

---

## **Endpoints de la API `/topicos`**

Todos los endpoints requieren **Basic Auth** con las credenciales mencionadas.

### 1️⃣ Crear un tópico
**POST /topicos**

- **URL:** `http://localhost:8080/topicos`  
- **Body (JSON):**

```json
{
  "titulo": "Mi primer tópico",
  "mensaje": "Mensaje de prueba desde Postman",
  "autor": "Emmanuel",
  "curso": "Spring Boot"
}
