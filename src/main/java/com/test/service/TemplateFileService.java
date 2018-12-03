package com.test.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Null;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateFileService {
    @Autowired
    freemarker.template.Configuration configuration;

    private String template = "";

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
        else if(templateID.equals("rela")){
            myname = configuration.getTemplate("jsonftl/namejs.ftl","UTF-8");
        }
        else if(templateID.equals("rst")){
            myname = configuration.getTemplate("rstftl/namerst.ftl","UTF-8");
        }


        content = FreeMarkerTemplateUtils.processTemplateIntoString(myname, name);
        String results=content;
        results=temp(jsonArrayTemplate,results,content,jsonName,templateID);

        template = results;
        //WriteStringToFile(jsonName,results,templateID);
        return results;
    }

    /**
     * json处理，取出mxCell的值（类型为json数组），从id=2的位置开始
     * jsonArrayTemplate是可用的json数组，用于jsonOp类进行进一步操作
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
        String sbReq = new String();
        String sbGoal = new String();
        String sbObs = new String();
        String sbRes = new String();
        int indexReq = 0;
        int indexGoal = 0;
        int indexObs = 0;
        int indexRes = 0;
        for(int j=0;j<jsonArray.size();j++){
            JSONObject job=jsonArray.getJSONObject(j);
            System.out.println(job);
            String flag;
            if(job.has("-flag ")) {
                flag=job.getString("-flag ");
                if (flag.equals("goal ")) {
                    String sbOneGoal;
                    Map<String, Object> model = new HashMap<String, Object>();
                    String value = job.getString("-value ");
                    if (job.has("-usecaseDiscription ")) {
                        String usecaseDiscription = job.getString("-usecaseDiscription ");
                        model.put("usecaseDescription", usecaseDiscription);
                    }
                    if (!job.has("-usecaseDiscription ")) {
                        model.put("usecaseDescription", "未定义");
                    }
                    if (job.has("-RefinesTo ")) {
                        String aftCondition = job.getString("-RefinesTo ");
                        model.put("RefinesTo", aftCondition);
                    }
                    if (!job.has("-RefinesTo ")) {
                        model.put("RefinesTo", "未定义");
                    }
                    if (job.has("-RefinedBy ")) {
                        String aftCondition = job.getString("-RefinedBy ");
                        model.put("RefinedBy", aftCondition);
                    }
                    if (!job.has("-RefinedBy ")) {
                        model.put("RefinedBy", "未定义");
                    }
                    if (job.has("-Obstructs ")) {
                        String aftCondition = job.getString("-Obstructs ");
                        model.put("Obstructs", aftCondition);
                    }
                    if (!job.has("-Obstructs ")) {
                        model.put("Obstructs", "未定义");
                    }
                    if (job.has("-Resolves ")) {
                        String aftCondition = job.getString("-Resolves ");
                        model.put("Resolves", aftCondition);
                    }
                    if (!job.has("-Resolves ")) {
                        model.put("Resolves", "未定义");
                    }
                    model.put("value", value);
                    Template t = null;
                    if(templateID.equals("md")){
                        t = configuration.getTemplate("mdftl/goalmd.ftl","UTF-8");
                    }
                    else if(templateID.equals("rela")){
                        t = configuration.getTemplate("jsonftl/goaljs.ftl","UTF-8");
                    }
                    else {
                        t = configuration.getTemplate("rstftl/goalrst.ftl","UTF-8");
                    }
                    indexGoal += 1;
                    model.put("index",indexGoal);
                    sbOneGoal = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    sbGoal += sbOneGoal;
                } else if (flag.equals("requirement ")) {
                    String sbOneReq;
                    Map<String, Object> model = new HashMap<String, Object>();
                    String value = job.getString("-value ");
                    if (job.has("-Description ")) {
                        String nonFunctionalRule = job.getString("-Description ");
                        model.put("Description", nonFunctionalRule);
                    }
                    if (!job.has("-Description ")) {
                        model.put("Description", "未定义");
                    }
                    if (job.has("-RefinesTo ")) {
                        String refines = job.getString("-RefinesTo ");
                        model.put("RefinesToReq", refines);
                    }
                    if (!job.has("-RefinesTo ")) {
                        model.put("RefinesToReq", "未定义");
                    }
                    if (job.has("-Agents ")) {
                        String agents = job.getString("-Agents ");
                        model.put("agents", agents);
                    }
                    if (!job.has("-Agents ")) {
                        model.put("agents", "未定义");
                    }
                    model.put("value", value);
                    Template t = null;
                    if(templateID.equals("md")){
                        t = configuration.getTemplate("mdftl/requirementmd.ftl","UTF-8");
                    }
                    else if(templateID.equals("rela")){
                        t = configuration.getTemplate("jsonftl/requirementjs.ftl","UTF-8");
                    }
                    else {
                        t = configuration.getTemplate("rstftl/requirementrst.ftl","UTF-8");
                    }
                    indexReq += 1;
                    model.put("index",indexReq);
                    sbOneReq = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    sbReq += sbOneReq;
                    //System.out.println(content);
                }
                else if (flag.equals("resource ")) {
                    if(job.has("-value ")) {
                        String sbOneRes;
                        Map<String, Object> model = new HashMap<String, Object>();
                        String value = job.getString("-value ");
                        if (job.has("-resourType ")) {
                            String resourceTypes = job.getString("-resourType ");
                            model.put("resourType",resourceTypes);
                        }
                        if (!job.has("-resourType ")) {
                            model.put("resourType", "未定义");
                        }
                        if (job.has("-RelateTo ")) {
                            String _relates = job.getString("-RelateTo ");
                            String relates = _relates.replace("</br>","---");
                            model.put("RelateTo", relates);
                        }
                        if (!job.has("-RelateTo ")) {
                            model.put("RelateTo", "未定义");
                        }
                        model.put("value", value);
                        Template t = null;
                        if (templateID.equals("md")) {
                            t = configuration.getTemplate("mdftl/resourcemd.ftl", "UTF-8");
                        }
                        else if(templateID.equals("rela")){
                            t = configuration.getTemplate("jsonftl/resourcejs.ftl", "UTF-8");
                        }
                        else {
                            t = configuration.getTemplate("rstftl/resourcerst.ftl", "UTF-8");
                        }
                        indexRes += 1;
                        model.put("index", indexRes);
                        sbOneRes = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                        sbRes += sbOneRes;
                    }
                }
                else if(flag.equals("obstacle ")){
                    String sbOneObs;
                    String value = job.getString("-value ");

                    Map<String, Object> model = new HashMap<String, Object>();
                    if (job.has("-gedetail ")) {
                        String detail = job.getString("-gedetail ");
                        model.put("detail", detail);
                    }
                    if (!job.has("-gedetail ")) {
                        model.put("detail", "未定义");
                    }
                    if (job.has("-Goal ")) {
                        String detail = job.getString("-Goal ");
                        model.put("Target", detail);
                    }
                    if (!job.has("-Goal ")) {
                        model.put("Target", "未定义");
                    }
                    model.put("value", value);
                    model.put("flag", flag);

                    Template t = null;
                    if(templateID.equals("md"))
                    {
                        t = configuration.getTemplate("mdftl/obstacle.ftl","UTF-8");
                    }
                    else if(templateID.equals("rela")){
                        t = configuration.getTemplate("jsonftl/obstaclejs.ftl","UTF-8");
                    }
                    else {
                        t = configuration.getTemplate("rstftl/obstacle.ftl","UTF-8");
                    }
                    indexObs += 1;
                    model.put("index",indexObs);
                    sbOneObs = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    //System.out.println(content);
                    sbObs += sbOneObs;
                }

                else if (!flag.equals("ellipse ")){
                    if(job.has("-value ")) {

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
                        if (templateID.equals("md")) {
                            t = configuration.getTemplate("mdftl/othersmd.ftl", "UTF-8");
                        }
                        else if(templateID.equals("rela")){
                            t = configuration.getTemplate("jsonftl/othersjs.ftl","UTF-8");
                        }
                        else {
                            t = configuration.getTemplate("rstftl/othersrst.ftl", "UTF-8");
                        }
                        content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                    }
                    //System.out.println(content);
                }
                //result += content;
            }
            else{

            }
        }
        //System.out.println(sbReq);
        result = result+sbGoal+sbObs+sbReq+sbRes;
        for(int i = result.length() - 1; i >= 0; i--){
            if(',' == result.charAt(i)){
                result = result.substring(0,i) + result.substring(i+1);
                break;
            }
        }
        String endStr = null;
        if(templateID.equals("rela")){
            endStr = "]}           ";
        }
        endStr = endStr + "\n\0\0\0\0\0\0\0\0\n\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n\0\0\0\0\0\0\0\0\n\0\0\0\0\0\0\0\0\n\n\n\n";

        result = result + endStr;

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
            if(templateID.equals("md") || templateID.equals("rela")) {
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
