package fr.uha.ensisa.antidemo.startup;

import fr.uha.ensisa.antidemo.entity.Image;
import fr.uha.ensisa.antidemo.entity.Video;
import fr.uha.ensisa.antidemo.repository.VideoRepository;
import fr.uha.ensisa.antidemo.service.ArticleService;
import fr.uha.ensisa.antidemo.service.ImageService;
import fr.uha.ensisa.antidemo.service.VideoService;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.context.ApplicationListener;

import fr.uha.ensisa.antidemo.entity.Article;
import fr.uha.ensisa.antidemo.service.ArticleService;
import fr.uha.ensisa.antidemo.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

@Component @RequiredArgsConstructor
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final int NUMBER_OF_ARTICLE = 1000;
    private final ArticleService articleService;
    private final ImageService imageService;
    private final VideoRepository videoRepo;

    public static byte[] getFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] bt = readInputStream(inStream);
            return bt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static byte[] readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[10240];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();

    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!articleService.getAll().isEmpty()) return;
        Random rand = new Random();
        String[] listOfImagUrl = {
          "https://svn.ensisa.uha.fr/bd/vid/halloween.png",
          "https://svn.ensisa.uha.fr/bd/vid/fantasy.jpg",
          "https://svn.ensisa.uha.fr/bd/vid/thunderstorm.jpg",
          "https://svn.ensisa.uha.fr/bd/vid/christmas.jpg",
          "https://svn.ensisa.uha.fr/bd/vid/sea.jpg",
          "https://svn.ensisa.uha.fr/bd/vid/beach.jpg",
          "https://svn.ensisa.uha.fr/bd/vid/seljalandsfoss.jpg",
          "https://svn.ensisa.uha.fr/bd/vid/waterfalls.jpg",
          "https://svn.ensisa.uha.fr/bd/vid/sunset.jpg"
        };

        int imgReplication = 10;
        try {
            for (; imgReplication > 0; imgReplication--) {
                for (int i = 0; i < listOfImagUrl.length; ++i) {
                    imageService.store(Image.builder().data(getFromNetByUrl(listOfImagUrl[i])).name(Path.of(listOfImagUrl[i]).getFileName().toString()).type("png").build());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String[] listOfVidUrl = {
            "https://svn.ensisa.uha.fr/bd/vid/big_buck_bunny.mp4"
        };

        try {
            for (int i = 0; i < listOfVidUrl.length; ++i) {
                videoRepo.save(Video.builder().name(Path.of(listOfVidUrl[i]).getFileName().toString()).type("video/mp4").data(getFromNetByUrl(listOfVidUrl[i])).build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String content =
          "# What is Lorem Ipsum?\n" +
          "\n" +
          "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n\n";
          
        for (int i = 0; i < NUMBER_OF_ARTICLE; i++) {
            articleService.save(Article.builder().content(content+"Image:"+(rand.nextInt(listOfImagUrl.length*5)+1)+"[200]\n\nVideo:"+(rand.nextInt(listOfVidUrl.length)+1)+"[280]").posterId(Long.valueOf(rand.nextInt(listOfImagUrl.length*5)+1)).title("Exemple - " + i).readCount(0l).build());
        }

    }

}
