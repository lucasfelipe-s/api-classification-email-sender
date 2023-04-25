package br.com.vivo.challengeform.view.controller;

import br.com.vivo.challengeform.dto.TechnologyDTO;
import br.com.vivo.challengeform.service.TechnologyService;
import br.com.vivo.challengeform.view.model.TechnologyRequest;
import br.com.vivo.challengeform.view.model.TechnologyResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    /**
     Retorna uma lista de objetos que representam as tecnologias disponíveis.
     @return uma lista de objetos
     */
    @GetMapping("/technologies")
    public ResponseEntity<List<TechnologyResponse>> getTechnologies(){

        List<TechnologyDTO> technologyDTOS= technologyService.getTechnologies();

        ModelMapper mapper = new ModelMapper();

        List<TechnologyResponse> technologyResponses = technologyDTOS.stream()
                .map(technologyDTO -> mapper.map(technologyDTO,TechnologyResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(technologyResponses, HttpStatus.OK);
    }

    /**
     Cria uma nova tecnologia com base nos dados fornecidos em um objeto TechnologyRequest.
     @param technologyRequest um objeto TechnologyRequest contendo os dados da nova tecnologia a ser criada.
     @return um objeto TechnologyResponse representando a nova tecnologia criada.
     */
    @PostMapping("/add-tech")
    @ResponseBody
    public ResponseEntity<TechnologyResponse> createTechnology(@RequestBody TechnologyRequest technologyRequest) {

        ModelMapper mapper =new ModelMapper();
        TechnologyDTO technologyDTO = mapper.map(technologyRequest, TechnologyDTO.class);

        technologyDTO =  technologyService.createTechnology(technologyDTO);

        return new ResponseEntity<>(mapper.map(technologyDTO, TechnologyResponse.class), HttpStatus.CREATED);
    }

    /**
     * Remove uma tecnologia do banco de dados pelo Id.
     * @param id da tecnologia que será removido.
     */
    @DeleteMapping("/tech/{id}")
    public ResponseEntity<?> deleteTechnology(@PathVariable long id) {
        technologyService.deleteTechnology(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
