package br.com.vivo.challengeform.entities;

import br.com.vivo.challengeform.enums.TechnologyTypes;
import br.com.vivo.challengeform.enums.TechnologyTypesConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Convert(converter = TechnologyTypesConverter.class)
	private TechnologyTypes profile = TechnologyTypes.NONE;

	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST,orphanRemoval = true)
	private List<Level> levels = new ArrayList<>();
}
