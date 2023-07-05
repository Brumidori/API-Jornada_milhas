package br.com.jornada.milhas.controller;

import br.com.jornada.milhas.model.Depoimento;
import br.com.jornada.milhas.repository.DepoimentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/depoimentos")
public class DepoimentoController {

    @Autowired
    private DepoimentoRepository repository;

    @GetMapping
    public ResponseEntity<List<Depoimento>> exibirDepoimentos(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Depoimento> getById(@PathVariable Long id){
        return repository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Depoimento> cadastrar(@RequestBody Depoimento dados){
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(dados));
    }


    @GetMapping("/home")
    public ResponseEntity<Page<Depoimento>> listar(@PageableDefault(size = 3, page = 0) Pageable paginacao) {
        Page<Depoimento> depoimentos = repository.findAll(paginacao);
        return ResponseEntity.ok(depoimentos);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Depoimento> atualizar(@RequestBody Depoimento dados){
        return repository.findById(dados.getId())
                .map(resposta -> ResponseEntity.status(HttpStatus.OK)
                .body(repository.save(dados)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Depoimento> depoimento = repository.findById(id);

        if(depoimento.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repository.deleteById(id);
    }
}
