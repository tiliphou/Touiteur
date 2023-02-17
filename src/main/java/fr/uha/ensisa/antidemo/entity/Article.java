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

  public String getHtml() {
    return this.getContent()
            .replaceAll("\n", "<br>")
            .replaceAll("Image:(\\d+)\\[(\\d+)\\]", "<img src=\"/image/$1\" height=\"$2px\">")
            .replaceAll("Image:(\\d+)", "<img src=\"/image/$1\">")
            .replaceAll("Video:(\\d+)\\[(\\d+)\\]", "<video src=\"/video/embed/$1\" height=\"$2px\" controls=\"\" autoplay=\"\"> </video>")
            .replaceAll("Video:(\\d+)", "<video src=\"/video/embed/$1\" controls=\"\" autoplay=\"\"> </video>");
  }
}
