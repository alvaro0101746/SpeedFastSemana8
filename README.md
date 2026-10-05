# SpeedFast - Sistema de Gestión de Pedidos y Entregas

Sistema de escritorio desarrollado en **Java (JDK 21)** e **Interfaz Gráfica (Swing)** para la gestión integral de repartidores, pedidos y entregas con persistencia en **MySQL**.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Interfaz Gráfica:** Java Swing (AWT / Swing Components)
* **Base de Datos:** MySQL 8.x
* **Conectividad:** JDBC (`mysql-connector-j-8.x`)
* **Patrón de Diseño:** DAO (Data Access Object) y MVC (Modelo-Vista-Controlador)

---

## 🗄️ Esquema de Base de Datos

```sql
CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE IF NOT EXISTS repartidores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(200) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS entregas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_repartidor INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_repartidor) REFERENCES repartidores(id) ON DELETE CASCADE
);


