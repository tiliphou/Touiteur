package fr.uha.ensisa.antidemo.controller.web;

import fr.uha.ensisa.antidemo.dao.IArticleService;
import fr.uha.ensisa.antidemo.dao.IImageService;
import fr.uha.ensisa.antidemo.entity.Article;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {
  @Autowired
  private ServletContext servletContext;

  private final IArticleService articleService;
  private final IArticleService imageService;


  @GetMapping("/")
  public String all(Model model) {
    return "redirect:/index";
  }


  @GetMapping("/index")
  public String index(Model model) {
    model.addAttribute("articles", articleService.getAll());
    return "index";
  }

  @GetMapping("/index/article/{id}")
  public String index(Model model, @PathVariable("id") Long id) {
    Article a = articleService.findByID(id);
    model.addAttribute("article", a);
    String content = injectMedia(a);
    Parser parser = Parser.builder().build();
    Node document = parser.parse(content);
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    articleService.incrementReads(id, 1);
    model.addAttribute("content", renderer.render(document));
    return "single";
  }

  String injectMedia(Article a) {
    String c = a.getContent();
    StringBuffer html = null;
    Pattern pattern = Pattern.compile("(Image|Video):(\\d+)(?:\\[(\\d+)(?:,(\\d+))?\\])?");
    Matcher matcher = pattern.matcher(c);
    int start = 0;
    while(matcher.find(start)) {
      if (html == null) html = new StringBuffer();
      String kind = matcher.group(1);
      String imgStr = matcher.group(2);
      String w = null, h = null;
      try {
        long imgId = Long.parseLong(imgStr);
        w = matcher.group(3);
        h = matcher.group(4);
        if (h == null && w != null) {
          h = w;
          w = null;
        }
        String medium = null;
        switch(kind) {
          case "Image": {
            String imgTitle = imageService.findByID(imgId).getTitle();
            medium = "<img src=\"" + servletContext.getContextPath() + "/image/" + imgId + "\" "
                    + (w == null ? "" : "width=\"" + w + "px\" ")
                    + (h == null ? "" : "height=\"" + h + "px\" ")
                    + (imgTitle == null ? "" : "alt=\"" + imgTitle + "\" title=\"" + imgTitle + "\" ")
                    + ">";
            break;
          }
          case "Video": {
            medium = "<video src=\"" + servletContext.getContextPath() + "/video/embed/" + imgId + "\" "
                    + (w == null ? "" : "width=\"" + w + "px\" ")
                    + (h == null ? "" : "height=\"" + h + "px\" ")
                    + "controls=\"\" autoplay=\"\" muted=\"\"></video>";
            break;
          }
          default: assert false : "Unknown medium " + kind;
        }
        html.append(c.substring(start, matcher.start()) + medium);
      } catch (NumberFormatException x) {
        html.append(c.substring(start, matcher.start()) + matcher.group(0));
      }
      start = matcher.end();
    }
    return html == null ? c : html.toString();
  }


  @GetMapping("/login")
  public String login(Model model, @RequestParam(required = false) String user) {
    model.addAttribute("login", user != null ? user : "");
    return "authentication";
  }

}
