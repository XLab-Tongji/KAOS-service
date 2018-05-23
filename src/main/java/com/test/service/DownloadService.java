package com.test.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class DownloadService {
    @Autowired
    freemarker.template.Configuration configuration;

    @Autowired
    MongoTemplate mongoTemplateModel;
    @Transactional
    public String getResult(String jsonName,String jsonGet,String templateID)
    throws IOException,TemplateException {
        StringBuffer sb=new StringBuffer(jsonGet);

        //get the useful jsonArray
        JSONArray jsonArrayTemplate=getUseful(sb);
        System.out.println(jsonArrayTemplate);
        String content;
        Map<String,Object> name=new HashMap<String, Object>();
        name.put("name",jsonName);

        Template myname = null;

        if(templateID.equals("md")){
            myname = configuration.getTemplate("mdftl/namemd.ftl","UTF-8");
        }
        else {
            myname = configuration.getTemplate("rstftl/namerst.ftl","UTF-8");
        }


        content = FreeMarkerTemplateUtils.processTemplateIntoString(myname, name);
        String results=content;
        results=temp(jsonArrayTemplate,results,content,jsonName,templateID);

        WriteStringToFile(jsonName,results,templateID);
        return results;
    }

    /**
     * json处理，取出mxCell的值（类型为json数组）
     * @param sb
     * @return
     */
    public JSONArray getUseful(StringBuffer sb){
        for(int i=0;i<sb.length();i++){
            if(sb.charAt(i)=='{'){
                if(sb.charAt(i+1)=='\\'&&sb.charAt(i+2)=='n'){
                    sb.replace(i+1,i+3,"");
                }

            }
            if(sb.charAt(i)=='['){
                if(sb.charAt(i+1)=='\\'&&sb.charAt(i+2)=='n'){
                    sb.replace(i+1,i+3,"");
                }
            }
            if(sb.charAt(i)=='"'){
                if(sb.charAt(i-1)=='\\'){
                    sb.replace(i-1,i," ");
                }
                if(sb.charAt(i+1)=='\\'&&sb.charAt(i+2)=='n'){
                    sb.replace(i+1,i+3,"");
                }
            }
            if((sb.charAt(i)==',')&&((sb.charAt(i-1)==',')||(sb.charAt(i-1)=='"')||(sb.charAt(i-1)=='}'))){
                if(sb.charAt(i+1)=='\\'&&sb.charAt(i+2)=='n'){
                    sb.replace(i+1,i+3,"");
                }
            }
            if(((sb.charAt(i)==']')||(sb.charAt(i)=='}'))&&i!=sb.length()-1){
                if(sb.charAt(i+1)=='\\'&&sb.charAt(i+2)=='n'){
                    sb.replace(i+1,i+3,"");
                }
            }
        }
        System.out.println(sb);
        String s=sb.toString();
        JSONObject jsonObject=JSONObject.fromObject(s);
        System.out.println(jsonObject);

        JSONArray myjsonArray=JSONArray.fromObject(jsonObject.getJSONObject("mxGraphModel ").getJSONObject("root ").getJSONArray("mxCell "));
        int length=myjsonArray.size();
        JSONArray jsonArrayTemplate=new JSONArray();
        for(int i=2;i<length;i++){
            JSONObject job=myjsonArray.getJSONObject(i);
            jsonArrayTemplate.add(job);
        }
        System.out.println(jsonArrayTemplate);
        return jsonArrayTemplate;
    }

    /**
     * 关系处理
     * @param jsonArray
     * @param result
     * @param content
     * @param jsonName
     * @param templateID
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String temp(JSONArray jsonArray,String result,String content,String jsonName,@PathVariable String templateID)
            throws IOException, TemplateException{
        for(int j=0;j<jsonArray.size();j++){
            JSONObject job=jsonArray.getJSONObject(j);
            System.out.println(job);
            String flag;
            if(job.has("-flag ")) {
                flag=job.getString("-flag ");
                if (flag.equals("goal ")) {
                    Map<String, Object> model = new HashMap<String, Object>();
                    String value = job.getString("-value ");
                    if (job.has("-usecaseDiscription ")) {
                        String usecaseDiscription = job.getString("-usecaseDiscription ");
                        model.put("usecaseDiscription", usecaseDiscription);
                    }
                    if (!job.has("-usecaseDiscription ")) {
                        model.put("usecaseDiscription", "未定义");
                    }
                    if (job.has("-participant ")) {
                        String participant = job.getString("-participant ");
                        model.put("participant", participant);
                    }
                    if (!job.has("-participant ")) {
                        model.put("participant", "未定义");
                    }
                    if (job.has("-preCondition ")) {
                        String preCondition = job.getString("-preCondition ");
                        model.put("preCondition", preCondition);
                    }
                    if (!job.has("-preCondition ")) {
                        model.put("preCondition", "未定义");
                    }
                    if (job.has("-aftCondition ")) {
                        String aftCondition = job.getString("-aftCondition ");
                        model.put("aftCondition", aftCondition);
                    }
                    if (!job.has("-aftCondition ")) {
                        model.put("aftCondition", "未定义");
                    }
                    model.put("value", value);
                    Template t = null;
                    if(templateID.equals("md")){
                        t = configuration.getTemplate("mdftl/goalmd.ftl","UTF-8");
                    }
                    else {
                        t = configuration.getTemplate("rstftl/goalrst.ftl","UTF-8");
                    }
                    content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

                    //goalModelRepository.save(goalModel);

                    //System.out.println(content);
                } else if (flag.equals("requirement ")) {
                    Map<String, Object> model = new HashMap<String, Object>();
                    String value = job.getString("-value ");
                    if (job.has("-basicEventFlow ")) {
                        String basicEventFlow = job.getString("-basicEventFlow ");
                        model.put("basicEventFlow", basicEventFlow);
                    }
                    if (!job.has("-basicEventFlow ")) {
                        model.put("basicEventFlow", "未定义");
                    }
                    if (job.has("-addtionEventFlow ")) {
                        String addtionEventFlow = job.getString("-addtionEventFlow ");
                        model.put("addtionEventFlow", addtionEventFlow);
                    }
                    if (!job.has("-addtionEventFlow ")) {
                        model.put("addtionEventFlow", "未定义");
                    }
                    if (job.has("-businessRule ")) {
                        String businessRule = job.getString("-businessRule ");
                        model.put("businessRule", businessRule);
                    }
                    if (!job.has("-businessRule ")) {
                        model.put("businessRule", "未定义");
                    }
                    if (job.has("-nonFunctionalRule ")) {
                        String nonFunctionalRule = job.getString("-nonFunctionalRule ");
                        model.put("nonFunctionalRule", nonFunctionalRule);
                    }
                    if (!job.has("-nonFunctionalRule ")) {
                        model.put("nonFunctionalRule", "未定义");
                    }
                    model.put("value", value);
                    Template t = null;
                    if(templateID.equals("md")){
                        t = configuration.getTemplate("mdftl/requirementmd.ftl","UTF-8");
                    }
                    else {
                        t = configuration.getTemplate("rstftl/requirementrst.ftl","UTF-8");
                    }
                    content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    //System.out.println(content);
                } else if (!flag.equals("ellipse ")){
                    String value = job.getString("-value ");
                    Map<String, Object> model = new HashMap<String, Object>();
                    if (job.has("-gedetail ")) {
                        String detail = job.getString("-gedetail ");
                        model.put("detail", detail);
                    }
                    if (!job.has("-gedetail ")) {
                        model.put("detail", "未定义");
                    }
                    model.put("value", value);
                    model.put("flag", flag);

                    Template t = null;
                    if(templateID.equals("md"))
                    {
                        t = configuration.getTemplate("mdftl/othersmd.ftl","UTF-8");
                    }
                    else {
                        t = configuration.getTemplate("rstftl/othersrst.ftl","UTF-8");
                    }
                    content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    //System.out.println(content);
                }
                result += content;
            }
            else{

            }
        }

        return result;
    }

    /**
     * 写入模版文件
     * @param name
     * @param result
     * @param templateID
     */
    public void WriteStringToFile(String name,String result,@PathVariable String templateID) {
        PrintStream ps=null;
        try {
            File file=null;
            if(templateID.equals("md")) {
                file = new File(ResourceUtils.getURL("src").getPath(),"main/resources/templates/files/md/"+name+".md");
            }else if(templateID.equals("rst")){
                file = new File(ResourceUtils.getURL("src").getPath(),"main/resources/templates/files/rst/"+name+".rst");
            }
            ps = new PrintStream(new FileOutputStream(file));
            ps.println(result);// 往文件里写入字符串

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            ps.close();
        }
    }
}
