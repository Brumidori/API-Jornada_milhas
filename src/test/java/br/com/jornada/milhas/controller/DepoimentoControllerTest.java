package br.com.jornada.milhas.controller;

import br.com.jornada.milhas.model.Depoimento;
import br.com.jornada.milhas.repository.DepoimentoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepoimentoControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DepoimentoRepository depoimentoRepository;
    @Autowired
    private DepoimentoController controller;

    @BeforeAll
    void start() {
        depoimentoRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Cadastrar Um Depoimento")
    public void deveCriarUmDepoimento() {
        HttpEntity<Depoimento> requisicao = new HttpEntity<Depoimento>(new Depoimento(0L,
                "foto.png", "destino muito bom", "Paulo"));

        ResponseEntity<Depoimento> resposta = testRestTemplate
                .exchange("/depoimentos", HttpMethod.POST, requisicao, Depoimento.class);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(requisicao.getBody().getNomePessoa(), resposta.getBody().getNomePessoa());
        assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());
        assertEquals(requisicao.getBody().getDepoimento(), resposta.getBody().getDepoimento());
    }

    @Test
    @Order(2)
    @DisplayName("Atualizar um Depoimento")
    public void deveAtualizarUmDepoimento() {

        Depoimento depoimento = depoimentoRepository.save(new Depoimento(0L,
                "foto.jpeg", "nova descricao", "Juliana"));

        Depoimento depoimentoUpdate = new Depoimento(depoimento.getId(),
                "novafoto.jpeg", "update", "Juliana Perez");

        HttpEntity<Depoimento> corpoRequisicao = new HttpEntity<Depoimento>(depoimentoUpdate);


        ResponseEntity<Depoimento> corpoResposta = testRestTemplate
                .exchange("/depoimentos", HttpMethod.PUT, corpoRequisicao, Depoimento.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNomePessoa(), corpoResposta.getBody().getNomePessoa());
        assertEquals(corpoRequisicao.getBody().getDepoimento(), corpoResposta.getBody().getDepoimento());
    }

    @Test
    @Order(3)
    @DisplayName("Listar todos os Depoimentos")
    public void deveMostrarTodosDepoimentos() {

        depoimentoRepository.save(new Depoimento(0L,
                "foto.jpge", "depoimentos", "Sabrina"));

        depoimentoRepository.save(new Depoimento(0L,
                "foto.jpeg", "nova descricao", "Juliana"));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/depoimentos", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());

    }

    @Test
    @Order(4)
    @DisplayName("Listar 3 Depoimentos")
    public void deveMostrarTresDepoimentos() {

        depoimentoRepository.save(new Depoimento(0L,
                "foto.jpge", "depoimentos", "Sabrina"));
        depoimentoRepository.save(new Depoimento(0L,
                "foto.jpge", "depoimentos", "Sabrina"));
        depoimentoRepository.save(new Depoimento(0L,
                "foto.jpge", "depoimentos", "Sabrina"));
        depoimentoRepository.save(new Depoimento(0L,
                "foto.jpge", "depoimentos", "Sabrina"));

        ResponseEntity<String> resposta;
        resposta = testRestTemplate
                .exchange("/depoimentos/home", HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Deletar Depoimento por id")
    public void testDelete() {
        Depoimento depoimento = depoimentoRepository.save(new Depoimento(1L,
                "foto.jpeg", "nova descricao", "Juliana"));
        long id = depoimento.getId();
        ResponseEntity<String> resposta;
        String url = UriComponentsBuilder.fromUriString("/depoimentos/{id}")
                .buildAndExpand(id)
                .toUriString();

        resposta = testRestTemplate
                .exchange(url, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("Retorno not found ao deletar e nao encontrar id")
    public void testDelete_WhenDepoimentoNotFound() {
        Long depoimentoId = 1L;

        ResponseEntity<String> resposta;
        String url = UriComponentsBuilder.fromUriString("/depoimentos/{id}")
                .buildAndExpand(depoimentoId)
                .toUriString();

        resposta = testRestTemplate
                .exchange(url, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

}
