package com.itheima.bolee.file.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AbsFileStorageHandler.java
 * @Description 文件存储处理抽象类
 */
public abstract class AbsFileStorageHandler {

    public static Map<String,String> metaMimeTypeMap = new HashMap<>();

    static {
        metaMimeTypeMap.put(".docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        metaMimeTypeMap.put(".doc","application/msword");
        metaMimeTypeMap.put(".ppt","application/x-ppt");
        metaMimeTypeMap.put(".xls","application/vnd.ms-excel");
        metaMimeTypeMap.put(".xhtml","text/html");
        metaMimeTypeMap.put(".htm","text/html");
        metaMimeTypeMap.put(".html","text/html");
        metaMimeTypeMap.put(".jpe","image/jpg");
        metaMimeTypeMap.put(".jpeg","image/jpg");
        metaMimeTypeMap.put(".jpg","image/jpg");
        metaMimeTypeMap.put(".png","image/jpg");
        metaMimeTypeMap.put(".mp4","video/mp4");
        metaMimeTypeMap.put(".wmv","video/x-ms-wmv");
        metaMimeTypeMap.put(".pdf","application/pdf");
        metaMimeTypeMap.put(".mp3","audio/mp3");
    }

    /***
     * @description 文件路径生成策略
     *
     * @param filename
     * @return
     * @return: java.lang.String
     */
    public String builderOssPath(String filename) {
        String separator = "/";
        StringBuilder stringBuilder = new StringBuilder(50);
        LocalDate localDate = LocalDate.now();
        String yeat = String.valueOf(localDate.getYear());
        stringBuilder.append(yeat).append(separator);
        String moth = String.valueOf(localDate.getMonthValue());
        stringBuilder.append(moth).append(separator);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        String day = formatter.format(localDate);
        stringBuilder.append(day).append(separator);
        stringBuilder.append(filename);
        return stringBuilder.toString();
    }

}
