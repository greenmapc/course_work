package ru.itis.teamwork.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;


@Controller
public class MediaLoadingController {

    @Value("${upload.path}")
    private String userImgPath;

    @GetMapping("/img/{imgFullName:.+}")
    public @ResponseBody byte[] getImg(@PathVariable(value = "imgFullName") String imgFullName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(userImgPath + imgFullName));
        return IOUtils.toByteArray(bis);
    }
}
