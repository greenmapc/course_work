package ru.itis.teamwork.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.teamwork.models.User;
import ru.itis.teamwork.models.UserMainImg;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Value("${upload.path}")
    private String uploadPath;

    public Optional<UserMainImg> saveFile(User user, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            file.transferTo(new File(uploadPath + resultFilename));

            UserMainImg userMainImg = new UserMainImg();
            userMainImg.setHashName(resultFilename.substring(0, resultFilename.lastIndexOf('.')));
            userMainImg.setOriginalName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf('.')));
            userMainImg.setType(FilenameUtils.getExtension(file.getOriginalFilename()));

            return Optional.of(userMainImg);
        }

        return Optional.empty();
    }
}
