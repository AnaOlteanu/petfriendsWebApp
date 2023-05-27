package com.example.petfriends.controller;

import com.example.petfriends.model.Image;
import com.example.petfriends.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @GetMapping({"post/getImage/{id}", "/user/getImage/{id}", "/pet/getImage/{id}","/event/getImage/{id}"})
    public void downloadImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<Image> picture = imageService.findById(id);
        log.info("Image for id {} is {} ", id, picture.isPresent());
        if (picture.isPresent()) {
            byte[] byteArray = new byte[picture.get().getImage().length];
            int i = 0;
            for (Byte wrappedByte : picture.get().getImage()) {
                byteArray[i++] = wrappedByte;
            }
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
