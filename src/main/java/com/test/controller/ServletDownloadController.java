package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class ServletDownloadController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ServletDownloadController(){
        super();
    }

    /**
     * 下载文档接口
     * @param fileName
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/ServletDownload1",method = RequestMethod.POST)
    public void doDownload(@RequestParam(value = "fileName")String fileName, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ajax download file");
        //String fileName = request.getParameter("fileName");
        String myPath = ResourceUtils.getURL("src").getPath()+"main/resources/templates/files";
        File file1 = new File(myPath+"/md/"+fileName+".md");
        String result;
        if(file1.exists()){
            response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".md");
        }
        else if(!file1.exists()){
            file1 = new File(myPath+"/rst/"+fileName+".rst");
            response.setHeader("Content-Disposition","attachment;filename=" + fileName  + ".rst");
        }
        else{
            result = "下载失败";
        }
        String dir1=myPath+"/md";
        String dir2=myPath+"/rst";
        response.setContentType("application/octet-stream");

        response.setContentLength((int) file1.length());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file1);
            byte[] buffer = new byte[128];
            int count = 0;
            while ((count = fis.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.getOutputStream().flush();
            response.getOutputStream().close();
            if(fis!=null){
                fis.close();
            }
        }
        delAllFile(dir1);
        delAllFile(dir2);
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                //delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}

