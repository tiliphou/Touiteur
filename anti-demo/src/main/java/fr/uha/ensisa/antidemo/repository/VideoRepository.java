package fr.uha.ensisa.antidemo.repository;

import fr.uha.ensisa.antidemo.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

}
