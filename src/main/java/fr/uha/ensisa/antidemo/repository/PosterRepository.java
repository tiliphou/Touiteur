package fr.uha.ensisa.antidemo.repository;

import fr.uha.ensisa.antidemo.entity.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosterRepository extends JpaRepository<Poster, Long> {
}
