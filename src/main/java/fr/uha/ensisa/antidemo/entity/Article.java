package fr.uha.ensisa.antidemo.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  @NotEmpty
  private String title;
  @NotEmpty
  @Column(columnDefinition="TEXT")
  private String content;
  @NotEmpty
  private Long posterId;
  @Basic
  @Temporal(TemporalType.TIMESTAMP)
  private java.util.Date creationDate;
}
