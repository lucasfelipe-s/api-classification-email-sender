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
@Table(name = "technologies")
public class Technology {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Convert(converter = TechnologyTypesConverter.class)
	@Column(nullable = false)
	private TechnologyTypes type;

	//@JsonIgnore
	@OneToMany(mappedBy = "technology")
	private List<Level> levels = new ArrayList<>();

	@Override
	public String toString() {
		return "Technology{" +
				"id=" + id +
				", name='" + name + '\'' +
				", type='" + type + '\'' +

				'}';
	}
}
