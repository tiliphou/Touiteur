package fr.uha.ensisa.antidemo.entity;

import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
  @NotEmpty
  @Column(nullable = false, columnDefinition = "bigint DEFAULT 0")
  private long readCount;
}
