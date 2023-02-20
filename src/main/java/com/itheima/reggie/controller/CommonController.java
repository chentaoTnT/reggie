package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/**/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 实现文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        // file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());
        // 使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString() + suffix;

        // 如果路径不存在，需要创建目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            // 目录不存在
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 实现图片的回显
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(@RequestParam String name,
                         HttpServletResponse response) {
        response.setContentType("image/jpeg");
        try (
            // 创建一个输入流，用于读取图片内容
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            // 创建一个输出流，用于将读取到的图片内容写到页面上
            ServletOutputStream outputStream = response.getOutputStream();
        ) {

            byte[] bytes = new byte[1024];
            while (bufferedInputStream.read(bytes) != -1) {
                outputStream.write(bytes, 0, 1024);
                outputStream.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
