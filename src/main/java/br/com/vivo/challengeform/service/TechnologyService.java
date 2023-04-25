package br.com.vivo.challengeform.service;

import br.com.vivo.challengeform.dto.TechnologyDTO;
import br.com.vivo.challengeform.entities.Technology;
import br.com.vivo.challengeform.exceptions.resources.ResourceNotFoundException;
import br.com.vivo.challengeform.repository.TechnologyRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    /**
     Retorna uma lista de objetos TechnologyDTO que representam as tecnologias disponíveis.
     @return uma lista de objetos TechnologyDTO
     */
    public List<TechnologyDTO> getTechnologies(){
        List<Technology> technologies =  technologyRepository.findAll();

        return technologies.stream()
                .map(technology -> new ModelMapper().map(technology, TechnologyDTO.class))
                .collect(Collectors.toList());
    }

    /**
     Cria uma nova tecnologia com base nos dados fornecidos em um objeto TechnologyDTO.
     @param technologyDTO um objeto TechnologyDTO contendo os dados da nova tecnologia a ser criada.
     @return um objeto TechnologyDTO representando a nova tecnologia criada.
     */
    public TechnologyDTO createTechnology(@Valid TechnologyDTO technologyDTO) {
        ModelMapper mapper = new ModelMapper();
        Technology technology = mapper.map(technologyDTO, Technology.class);
        technology = technologyRepository.save(technology);

        technologyDTO.setId(technology.getId());

        return technologyDTO;
    }

    /**
     * Metodo que remove uma tecnologia do banco de dados pelo Id.
     * @param id da tecnologia que será removido.
     */
    public void deleteTechnology(long id) {
        // verificar se o usuário existe
        Optional<Technology> technology = technologyRepository.findById(id);

        if(technology.isEmpty()){
            throw new ResourceNotFoundException("Não foi possível deletar um usuário com o id:"+id+" - Usuário não existe");
        }

        technologyRepository.deleteById(id);
    }
}
