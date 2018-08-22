package com.example.water.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by  waiter on 18-8-22  上午12:41.
 *
 * @author waiter
 */
@org.springframework.stereotype.Controller
public class Controller {
    @Value("${app.dir}")
    private String path  ;
    /**
     * 获取file.html页面
     */
    @RequestMapping("/file")
    public String file(){
        return "file/file";
    }

    /**
     * 实现文件上传
     * */
    @RequestMapping("/file/fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("fileName") MultipartFile file,double version,String description){
        if(file.isEmpty()){
            return "false";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);


        File dest = new File(path + "/"+version+"/" + fileName);
        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            boolean mkdir = dest.getParentFile().mkdir();
        }
        try {
            //保存文件
            file.transferTo(dest);
            File file1 = new File(dest.getPath() + ".txt");
            PrintWriter printWriter = new PrintWriter(file1);
            printWriter.print(description);
            printWriter.close();
            return "true";
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
    }

    @RequestMapping(value = {"/","/wellcome"})
    public String wellCome(Model model) throws IOException {
        File dir = new File(path);
        if(null!=dir&&dir.isDirectory()){
            File[] files = dir.listFiles();
            if(null!=files) {
                Map<String, String> map = new HashMap<>(5);
                for (int i = 0; i < files.length; i++) {
                    File[] files1 = files[i].listFiles();
                    StringBuffer s=new StringBuffer();
                    if(files1!=null){
                        for (int j = 0; j < files1.length; j++) {
                            String absolutePath = files1[j].getAbsolutePath();
                            if (absolutePath.endsWith(".txt")){
                                File file = new File(absolutePath);
                                BufferedReader reader = new BufferedReader(new FileReader(file));
                                String s1=null;
                                while ((s1=reader.readLine())!=null){
                                    s.append(s1);
                                }
                                reader.close();
                            }
                        }
                    }
                    String absolutePath = files[i].getAbsolutePath();
                    String substring = absolutePath.substring(absolutePath.lastIndexOf("/" + 1));
                    map.put(substring.substring(1),s.toString());

                }

                model.addAttribute("map",map);
            }
        }

        return "wellcome";
    }

    @RequestMapping("/download/app")
    public String downLoad(double version,HttpServletResponse response) throws IOException {
        File files = new File(path + "/" + version);
        File[] files1 = files.listFiles();
        File file=null;
        for (int j = 0; j < files1.length; j++) {
            String absolutePath = files1[j].getAbsolutePath();
            if (absolutePath.endsWith(".apk")){
                file = files1[j].getCanonicalFile();
                break;
            }
        }
        //判断文件父目录是否存在
        if(file.exists()){
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + file.getName());
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}
