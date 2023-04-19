package br.com.vivo.challengeform.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	private String email;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_skill",
			joinColumns = @JoinColumn(name = "id_user"),
			inverseJoinColumns = @JoinColumn(name = "id_skill"))
	private List<Technology> skills;
}
