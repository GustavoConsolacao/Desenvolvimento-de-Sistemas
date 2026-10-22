USE sistema_cadastro;

-- Adiciona o nome completo à tabela de usuários existente.
ALTER TABLE usuarios ADD COLUMN nome VARCHAR(100) NULL;

-- Se já existia o usuário de teste, informe um nome para ele.
UPDATE usuarios SET nome = 'Administrador' WHERE nome IS NULL;

-- Novas contas terão nome obrigatório pela aplicação.
-- O código Java salva as senhas novas com BCrypt.
