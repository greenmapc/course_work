package ru.itis.teamwork.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.InputStream;


@Controller
public class ImgLoadingController {

    @Value("${mainImg.path}")
    private String path;

    private ServletContext servletContext;

    @GetMapping("/img/{imgName}.{imgType}")
    public @ResponseBody byte[] getImg(@PathVariable(value = "imgName") String imgName,
                                       @PathVariable(value = "imgType") String imgType) throws IOException {
        InputStream is = servletContext.getResourceAsStream(path + imgName + "." + imgType);
        return IOUtils.toByteArray(is);
    }

    @Autowired
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
