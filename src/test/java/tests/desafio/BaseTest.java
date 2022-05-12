package tests.desafio;

import Utils.Constantes;



import static io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;
import org.junit.Test;
import tests.desafio.ClassAuxDesafio.Conta;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class BaseTest implements Constantes {
    Random random                                  = new Random();
    int randomNumber                               = random.nextInt(1000 + 1 - 0) + 0;
    Conta conta                                    = new Conta( ("contaNovaCriadaTest"+randomNumber),false);
    Map<Object, Object> recebeDadosLogin           = new HashMap<Object,Object>();
    Map<String, String> enviaLogin                 = new HashMap<String,String>();
    Map<Object, Object> recebeDadosContaCadastrada = new HashMap<Object,Object>();
    Map<Object,Object> cadastroTransacao           = new HashMap<Object, Object>();
    Map<Object,Object> dadosTransacaoRecebido      = new HashMap<Object, Object>();

    @BeforeClass
    public static void setup(){
        baseURI = BASE_URL;

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setContentType(BASE_CONTENT_TYPE);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectResponseTime(lessThanOrEqualTo(MAX_TIMEOUT));
        responseSpecification = responseSpecBuilder.build();

        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void naoDeveAcessarAPISemToken(){
        given()
                .when()
                    //verbos http
                    .get("/contas")
                .then()
                    .log().all()
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
                                    .log().all()
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
                    .log().all()
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
                            .log().all()
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
                        .log().all()
                    .when()
                    //verbos http
                        .post("/contas")
                    .then()
                        .log().all()
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
                            .log().all()
                        .when()
                        //verbos http
                            .post("/transacoes")
                        .then()
                            .log().all()
                            .statusCode(201)
                            .body("conta_id", is(cadastroTransacao.get("conta_id")))
                            .body("usuario_id", is(cadastroTransacao.get("usuario_id")))
                            .body("data_transacao", is(notNullValue()))
                            .body("valor", containsString(cadastroTransacao.get("valor").toString()))
                            .extract().jsonPath().getMap("")
                ;
    }

}
