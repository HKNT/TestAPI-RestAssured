package tests.desafio;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;

import Utils.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainTest extends BaseTest {
    Random random                                  = new Random();
    int randomNumber                               = random.nextInt(1000 + 1);
    Conta conta                                    = new Conta( ("contaNovaCriadaTest"+randomNumber),false);
    Map<Object, Object> recebeDadosLogin           = new HashMap<>();
    Map<String, String> enviaLogin                 = new HashMap<>();
    Map<Object, Object> recebeDadosContaCadastrada = new HashMap<>();
    Map<Object,Object> cadastroTransacao           = new HashMap<>();
    Map<Object,Object> dadosTransacaoRecebido      = new HashMap<>();

    @Test
    public void naoDeveAcessarAPISemToken(){
        given()
                .when()
                //verbos http
                .get("/contas")
                .then()
                .statusCode(401)
                .body(is("Unauthorized"))
        ;
    }

    @Test
    public void deveFazerLoginComSucesso(){
        enviaLogin.put("email", "testea1@teste.com");
        enviaLogin.put("senha", "12345");

        recebeDadosLogin = given()
                .when()
                //verbos http
                .body(enviaLogin)
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().jsonPath().getMap("");
    }

    @Test
    public void deveIncluirContaComSucess(){
        deveFazerLoginComSucesso();

        recebeDadosContaCadastrada =
                given()
                        .header("Authorization","JWT "+recebeDadosLogin.get("token"))
                        .body(conta)
                        .when()
                        //verbos http
                        .post("/contas")
                        .then()
                        .statusCode(201)
                        .body("nome", is(conta.getNome()))
                        .extract().jsonPath().getMap("")
        ;
    }

    @Test
    public void deveAlterarConta(){
        deveFazerLoginComSucesso();

        conta.setNome("contaAlteradaTest"+randomNumber);
        conta.setVisivel(true);

        recebeDadosContaCadastrada =
                given()
                        .header("Authorization","JWT "+recebeDadosLogin.get("token"))
                        .body(conta)
                        .when()
                        //verbos http
                        .post("/contas")
                        .then()
                        .statusCode(201)
                        .body("nome", is(conta.getNome()))
                        .extract().jsonPath().getMap("")
        ;
    }

    @Test
    public void naoDeveIncluirContaRepetida(){
        deveFazerLoginComSucesso();

        conta.setNome("teste3");

        given()
                .header("Authorization","JWT "+recebeDadosLogin.get("token"))
                .body(conta)
                .when()
                //verbos http
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", containsString("existe uma conta com esse nome"))
        ;
    }

    @Test
    public void deveInserirMovimentacao(){
        deveFazerLoginComSucesso();
        deveIncluirContaComSucess();

        cadastroTransacao.put("conta_id",recebeDadosContaCadastrada.get("id"));
        cadastroTransacao.put("usuario_id", recebeDadosContaCadastrada.get("usuario_id"));
        cadastroTransacao.put("descricao","descricaoTest");
        cadastroTransacao.put("envolvido","envolvidoTest");
        cadastroTransacao.put("tipo","REC");
        cadastroTransacao.put("data_transacao","10/05/2022");
        cadastroTransacao.put("data_pagamento","10/05/2022");
        cadastroTransacao.put("valor", 1000.99f);
        cadastroTransacao.put("status",false);


        dadosTransacaoRecebido =
                given()
                        .header("Authorization","JWT "+recebeDadosLogin.get("token"))
                        .body(cadastroTransacao)
                        .when()
                        //verbos http
                        .post("/transacoes")
                        .then()
                        .statusCode(201)
                        .body("conta_id", is(cadastroTransacao.get("conta_id")))
                        .body("usuario_id", is(cadastroTransacao.get("usuario_id")))
                        .body("data_transacao", is(notNullValue()))
                        .body("valor", containsString(cadastroTransacao.get("valor").toString()))
                        .extract().jsonPath().getMap("")
        ;
    }
}
