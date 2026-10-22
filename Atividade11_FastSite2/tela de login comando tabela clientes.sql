
USE sistema_cadastro;
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(150),
    cep VARCHAR(15),
    numero VARCHAR(20),
    telefone VARCHAR(20),
    observacao TEXT
);