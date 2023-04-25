package br.com.vivo.challengeform.repository;

import br.com.vivo.challengeform.enums.TechnologyTypes;
import br.com.vivo.challengeform.entities.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    @Query("SELECT tech.type, COUNT(tech) FROM Technology tech GROUP BY tech.type")
    Map<TechnologyTypes, Long> countByTechnologyType();

    Technology findFirstByName(String name);
}
