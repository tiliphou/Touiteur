package fr.uha.ensisa.antidemo.repository;

import fr.uha.ensisa.antidemo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
