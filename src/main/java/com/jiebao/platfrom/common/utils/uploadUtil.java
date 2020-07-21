package com.jiebao.platfrom.common.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class uploadUtil {
    public static List<String> upload(MultipartFile[] files, HttpServletRequest request) {
        List<String> fileList = new ArrayList<>();   //存储    文件地址
        String path = "127.0.0.1:" + request.getLocales()+"/files";
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                if (!file.isEmpty()) {
                    try {
                        File filePath = new File(path);  //目录名
                        if (!filePath.exists())
                            filePath.mkdirs();
                        String savePath = path + file.getOriginalFilename();
                        file.transferTo(new File(savePath));
                        fileList.add(savePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return fileList;
    }
}
