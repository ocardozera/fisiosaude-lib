

--
-- INSERT INTO CURSO(nome, categoria) VALUES('Spring Boot', 'Programação');
-- INSERT INTO CURSO(nome, categoria) VALUES('HTML 5', 'Front-end');
--
-- INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida', 'Erro ao criar projeto', '2019-05-05 18:00:00', 'NAO_RESPONDIDO', 1, 1);
-- INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 2', 'Projeto não compila', '2019-05-05 19:00:00', 'NAO_RESPONDIDO', 1, 1);
-- INSERT INTO TOPICO(titulo, mensagem, data_criacao, status, autor_id, curso_id) VALUES('Dúvida 3', 'Tag HTML', '2019-05-05 20:00:00', 'NAO_RESPONDIDO', 1, 2);

INSERT INTO ESTADO(nome, sigla) VALUES('Paraná', 'PR');
INSERT INTO ESTADO(nome, sigla) VALUES('Mato Grosso', 'MT');
INSERT INTO ESTADO(nome, sigla) VALUES('São Paulo', 'SP');
INSERT INTO ESTADO(nome, sigla) VALUES('Santa Catarina', 'SC');
INSERT INTO ESTADO(nome, sigla) VALUES('Mato Grosso do Sul', 'MS');


INSERT INTO CIDADE(nome, estado_id) VALUES ('Paranavaí', 1);
INSERT INTO CIDADE(nome, estado_id) VALUES ('Maringá', 1);
INSERT INTO CIDADE(nome, estado_id) VALUES ('São Paulo', 3);
INSERT INTO CIDADE(nome, estado_id) VALUES ('Joinville', 4);
INSERT INTO CIDADE(nome, estado_id) VALUES ('Várzea Grande', 2);
INSERT INTO CIDADE(nome, estado_id) VALUES ('Cuiabá', 2);
INSERT INTO CIDADE(nome, estado_id) VALUES ('Campo Grande', 5);

INSERT INTO SERVICO(nome, valor_sessao, maximo_alunos_sessao) VALUES ('Fisioterapia', 50.00, 1);
INSERT INTO SERVICO(nome, valor_sessao, maximo_alunos_sessao) VALUES ('Pilates', 80.00, 2);

INSERT INTO USUARIO(nome, email, senha) VALUES('Aluno', 'aluno@email.com', '$2a$10$L4SH6GlpEaoepv/BdzG/N..vWIUOHZEGUGXDyoOI9FfKj8MgCKAUu');

INSERT INTO USUARIO(nome, email, senha, telefone, cpf, data_nascimento, cidade_id,
                    logradouro, numero, complemento, bairro, administrador, fisioterapeuta,
                    registro_profissional, paciente) VALUES ('Admin', 'admin@gmail.com', '$2a$10$L4SH6GlpEaoepv/BdzG/N..vWIUOHZEGUGXDyoOI9FfKj8MgCKAUu',
                                                             '44991361481', '06565435901', '2000-07-29', 1, 'Av Roosevelt', 217, 'Casa c/ antena',
                                                             'Jd São Jorge', true, false, null, false);

INSERT INTO USUARIO(nome, email, senha, telefone, cpf, data_nascimento, cidade_id,
                    logradouro, numero, complemento, bairro, administrador, fisioterapeuta,
                    registro_profissional, paciente) VALUES ('Fisioterapeuta 1', 'fisio1@gmail.com', '$2a$10$L4SH6GlpEaoepv/BdzG/N..vWIUOHZEGUGXDyoOI9FfKj8MgCKAUu',
                                                             '44991361481', '06565435901', '2000-07-29', 2, 'Av Roosevelt', 217, 'Casa c/ antena',
                                                             'Jd São Jorge', false, true, 00001, false);

INSERT INTO USUARIO(nome, email, senha, telefone, cpf, data_nascimento, cidade_id,
                    logradouro, numero, complemento, bairro, administrador, fisioterapeuta,
                    registro_profissional, paciente) VALUES ('Fisioterapeuta 2', 'fisio2@gmail.com', '$2a$10$L4SH6GlpEaoepv/BdzG/N..vWIUOHZEGUGXDyoOI9FfKj8MgCKAUu',
                                                             '44991361481', '06565435901', '2000-07-29', 3, 'Av Roosevelt', 217, 'Casa c/ antena',
                                                             'Jd São Jorge', false, true, 00002, false);

INSERT INTO USUARIO(nome, email, senha, telefone, cpf, data_nascimento, cidade_id,
                    logradouro, numero, complemento, bairro, administrador, fisioterapeuta,
                    registro_profissional, paciente) VALUES ('Paciente 1', 'paciente1@gmail.com', '$2a$10$L4SH6GlpEaoepv/BdzG/N..vWIUOHZEGUGXDyoOI9FfKj8MgCKAUu',
                                                             '44991361481', '06565435901', '2000-07-29', 4, 'Av Roosevelt', 217, 'Casa c/ antena',
                                                             'Jd São Jorge', false, false, null, true);




INSERT INTO SERVICO_FISIOTERAPEUTA(servico_id, fisioterapeuta_id) VALUES (1, 1);
INSERT INTO SERVICO_FISIOTERAPEUTA(servico_id, fisioterapeuta_id) VALUES (2, 2);

INSERT INTO AGENDAMENTO(fisioterapeuta_id, paciente_id, servico_id, status, data_inclusao, data_agendamento, inicio_agendamento, fim_agendamento) VALUES (
            1, 1, 1, 1, '2022-10-12 15:15:00', '2022-10-12', '2022-10-12 17:00:00', '2022-10-12 18:00:00');



