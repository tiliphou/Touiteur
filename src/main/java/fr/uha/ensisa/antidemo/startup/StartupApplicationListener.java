package fr.uha.ensisa.antidemo.startup;

import fr.uha.ensisa.antidemo.entity.Image;
import fr.uha.ensisa.antidemo.service.ArticleService;
import fr.uha.ensisa.antidemo.service.ImageService;
import org.springframework.context.ApplicationListener;

import fr.uha.ensisa.antidemo.entity.Article;
import fr.uha.ensisa.antidemo.service.ArticleService;
import fr.uha.ensisa.antidemo.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

@Component @RequiredArgsConstructor
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final int NUMBER_OF_ARTICLE = 1000;
    private final ArticleService articleService;
    private final ImageService imageService;

    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] btImg = readInputStream(inStream);
            return btImg;
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
          "https://cdn.pixabay.com/photo/2017/10/10/16/55/halloween-2837936_1280.png",
          "https://cdn.pixabay.com/photo/2018/01/12/10/19/fantasy-3077928_1280.jpg",
          "https://cdn.pixabay.com/photo/2017/10/17/16/10/fantasy-2861107_1280.jpg",
          "https://cdn.pixabay.com/photo/2018/05/30/15/39/thunderstorm-3441687_1280.jpg",
          "https://cdn.pixabay.com/photo/2016/12/16/15/25/christmas-1911637_1280.jpg",
          "https://cdn.pixabay.com/photo/2013/07/18/20/26/sea-164989_1280.jpg",
          "https://cdn.pixabay.com/photo/2014/08/15/11/29/beach-418742_1280.jpg",
          "https://cdn.pixabay.com/photo/2016/10/18/21/28/seljalandsfoss-1751463_1280.jpg",
          "https://cdn.pixabay.com/photo/2016/01/16/22/15/waterfalls-1144130_1280.jpg",
          "https://cdn.pixabay.com/photo/2016/05/05/02/37/sunset-1373171_1280.jpg"
        };

        try {
            for (int i = 0; i < listOfImagUrl.length; ++i) {
                imageService.store(Image.builder().data(getImageFromNetByUrl(listOfImagUrl[i])).name("test 1").type("png").build());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String content = "\n" +
          "What is Lorem Ipsum?\n" +
          "\n" +
          "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n";
        for (int i = 0; i < NUMBER_OF_ARTICLE; i++) {
            articleService.save(Article.builder().content(content).posterId(Long.valueOf(rand.nextInt(listOfImagUrl.length - 2))).title("Exemple - " + i).build());
        }

    }

}
