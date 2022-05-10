package fr.uha.ensisa.antidemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  @NotEmpty
  private String title;
  @NotEmpty
  private String content;
  @NotEmpty
  private Long posterId;
}
