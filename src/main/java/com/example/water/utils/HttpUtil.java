package com.example.water.utils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class HttpUtil {

    public static String urll = "http://api.map.baidu.com/telematics/v3/weather?location=%E5%8C%97%E4%BA%AC&output=json&ak=uiQdHkU6HMAap4Pe74HdIcqe";
    static URL url = null;

   

    /**
     * 向数据库传数据
     * @param path 请求地址
     * @param params 参数
     * @return 描述
     */
    public static String sendPostMessage(String path,Map<String,String> params)
    {

        try {
            return sendPostMessage(path,params, "utf-8");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return "";
    }

    /**
     * 向数据库传数据
     * @param path
     * @param params
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String sendPostMessage(String path,Map<String,String> params,String encode) throws UnsupportedEncodingException
    {

        StringBuffer buffer = new StringBuffer();
        if(!params.isEmpty()&&params.size()!=0)
        {
            for(Map.Entry<String, String> entry:params.entrySet())
            {
                buffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode)).append("&");

            }
            buffer.deleteCharAt(buffer.length()-1);
            System.out.println(buffer);
            try {

                url = new URL(path);
                HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
                httpCon.setReadTimeout(5000);
                httpCon.setDoInput(true);
                httpCon.setDoOutput(true);
                byte mydata[];
                mydata = buffer.toString().getBytes("utf-8");
                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Cotent-Type", "application/x-www-form-urlenconded");
                httpCon.setRequestProperty("Content-Length",String.valueOf(mydata.length));
                httpCon.setRequestProperty("Charset", "utf-8");
                OutputStream outStream = httpCon.getOutputStream();
                outStream.write(mydata,0,mydata.length);
                outStream.close();
                int respondCode = httpCon.getResponseCode();
                System.out.println(respondCode);
                if(200==respondCode)
                {
                    return changeInputStream(httpCon.getInputStream(),encode);
                }
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        return "";


    }


    /**
     *
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeInputStream(InputStream inputStream, String encode) {


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String result;
        int length = 0;
        byte data[] = new byte[1024];
        try {
            while((length=inputStream.read(data))!=-1)
            {
                out.write(data, 0, length);

            }
            result = new String(out.toByteArray(), encode);
            return result;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }




}
