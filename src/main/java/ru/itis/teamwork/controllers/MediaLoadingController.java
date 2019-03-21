package ru.itis.teamwork.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;


@Controller
public class MediaLoadingController {

    @Value("${mainImg.path}")
    private String userImgPath;

    private ServletContext servletContext;

    @GetMapping("/img/{imgFullName:.+}")
    public @ResponseBody byte[] getImg(@PathVariable(value = "imgFullName") String imgFullName) throws IOException {
        InputStream is = servletContext.getResourceAsStream(userImgPath + imgFullName);
        return IOUtils.toByteArray(is);
    }

    @Autowired
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
