package br.com.vivo.challengeform.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "levels")
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int value;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "technology_id", referencedColumnName = "id")
    private Technology technology;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

//    public void setTechnology(Optional<Technology> byId) {
//    }

    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", value=" + value +
                ", technology=" + technology +
                '}';
    }
}
