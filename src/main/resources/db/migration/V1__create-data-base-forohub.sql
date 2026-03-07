-- Tabla Perfil
CREATE TABLE perfiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rol VARCHAR(100) NOT NULL
);

-- Tabla Usuario
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla Usuario Perfil (relación muchos a muchos)
CREATE TABLE usuario_perfil (
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    CONSTRAINT fk_usuario_perfil_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_usuario_perfil_perfil  FOREIGN KEY (perfil_id)  REFERENCES perfiles(id)
);

-- Tabla Curso
CREATE TABLE cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla Tópico
CREATE TABLE topicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    mensaje VARCHAR(400) NOT NULL,
    fecha_creacion DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'ABIERTO',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    CONSTRAINT fk_topico_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id),
    CONSTRAINT fk_topico_curso FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

-- Tabla Respuesta
CREATE TABLE respuestas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensaje VARCHAR(400) NOT NULL,
    topico_id BIGINT NOT NULL,
    fecha_creacion  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    autor_id BIGINT NOT NULL,
    solucion BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_respuesta_topico FOREIGN KEY (topico_id) REFERENCES topicos(id),
    CONSTRAINT fk_respuesta_autor FOREIGN KEY (autor_id)  REFERENCES usuarios(id)
);