# TestAPI-RestAssured
Projeto de teste de API utilizando o framework RestAssured para o JAVA. Utilizei de um desafio encontrado na internet para criar este portfolio, assim demonstrando o meu conhecimento. 

                                                      ==Desafio==
                                                      
Desafio:

Cenarios

basePath: https://barrigarest.wcaquino.me/

Não deve acessar a API sem token**

GET /contas

=======================
Deve incluir uma conta com sucesso**

POST /signin

Enviar:
(mandar email e senha)
testea1@teste.com
12345

Obter Token:

POST /contas
Enviar:
nomeConta: hgTestea1


===============

Deve alterar conta com sucesso**

PUT /contas/ID
Enviar:
novo nome da conta
===============

não deve incluir conta com nome repetido**

POST /contas/

envia: nome

===============

Deve inserir movimentação com sucesso
POST /transacoes

enviar:
conta_id
usuario_id
descricao
envolvido
tipo (DESP / REC)
data_transacao (dd/MM/YYYY)
data_pagamento (dd/MM/YYYY)
valor (0.000f)
status (true/false)

===============

Deve validar campos obrigatorios na movimentação

POST /transacoes

===============

nao deve cadastrar movimentação futura

POST /transacoes

===============
não deve remover uma conta com movimentação

DELETE /contas/ID

===============

deve calcular saldo das contas

GET /saldo

===============

deve remover uma movimentação

DELETE /transacoes/id


                                                      ==Resultado do desafio==
                                                    
![image](https://user-images.githubusercontent.com/39340785/168004230-31964d0c-acd7-4e91-9c56-22778cf05e65.png)

