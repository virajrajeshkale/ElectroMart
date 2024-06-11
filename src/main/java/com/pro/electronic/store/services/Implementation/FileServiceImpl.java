package com.pro.electronic.store.services.Implementation;

import com.pro.electronic.store.exceptions.BadApiRequest;
import com.pro.electronic.store.services.FileService;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String fileName  = UUID.randomUUID().toString();
        String extension  = originalFileName.substring(originalFileName.indexOf('.'));
        String fileNameWithExtension = fileName+extension;
        String fullPathWithFileName = path+ fileNameWithExtension;

        //allow only jpg,png,JPEG file
        if(extension.equalsIgnoreCase(".png")||extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg"))
        {
                File folder = new File(path);
                //if folder is not exist for file
                if(!folder.exists())
                    folder.mkdirs();
                //upload file

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
                return  fileNameWithExtension;
        }else
        {
            throw  new BadApiRequest("File Name with this "+extension+"Not Allowed..!!");
        }

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path+File.separator+name;
        InputStream fileInputStream = new FileInputStream(fullPath);
        return fileInputStream;
    }
}
