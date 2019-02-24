package com.example.water.controller;

import com.example.water.model.UpdateInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 欢迎页
     * @param model
     * @return
     * @throws IOException
     */
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

    /**
     * app检查更新接口
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/app/update")
    public UpdateInfo update(HttpServletRequest request,HttpServletResponse response) throws IOException{
        UpdateInfo updateInfo = new UpdateInfo();
        File dir = new File(path);
        String[] list = dir.list();
        Assert.notNull(list,"没有任何app版本");
        Assert.notEmpty(list,"没有任何app版本");
        double v=1.0;
        for (String l:list) {
            double v1 = Double.parseDouble(l);
            if (v1>v){
                v=v1;
            }
        }

        updateInfo.setServerVersion(v);
        updateInfo.setAppname("终端水管理系统");
        updateInfo.setServerFlag(0+"");
        updateInfo.setLastForce(0+"");

        File files = new File(path + "/" + v);
        File[] files1 = files.listFiles();
        StringBuffer s=new StringBuffer();
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

        updateInfo.setUpgradeinfo(s.toString());
        StringBuffer requestURL = request.getRequestURL();
        StringBuffer replace = requestURL.replace(requestURL.indexOf("/app/update"), requestURL.length() , "/download/app");
        replace.append("?version="+v);
        updateInfo.setUpdateUrl(replace.toString());
        return updateInfo;
    }

    /**
     * app下载接口
     * @param version
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/download/app")
    public ResponseEntity<InputStreamResource> downLoad(double version, HttpServletResponse response) throws IOException {
        File files = new File(path + "/" + version);
        File[] files1 = files.listFiles();
        String path=null;
        for (int j = 0; j < files1.length; j++) {
            String absolutePath = files1[j].getAbsolutePath();
            if (absolutePath.endsWith(".apk")){
                path = files1[j].getCanonicalPath();
                break;
            }
        }

        FileSystemResource file = new FileSystemResource(path);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }
}
