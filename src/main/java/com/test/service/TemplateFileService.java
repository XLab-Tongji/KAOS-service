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
        String sbHex = new String();
        String sbDom = new String();
        int indexReq = 0;
        int indexGoal = 0;
        int indexObs = 0;
        int indexRes = 0;
        int indexHex = 0;
        int indexDom = 0;
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
                    value = value.substring(0,value.length()-1);
                    if (job.has("-usecaseDiscription ")) {
                        String usecaseDiscription = job.getString("-usecaseDiscription ");
                        usecaseDiscription = usecaseDiscription.substring(0,usecaseDiscription.length()-1);
                        if(usecaseDiscription.equals("undefined")){
                            model.put("usecaseDescription", "  ");
                        }
                        else {
                            model.put("usecaseDescription", usecaseDiscription);
                        }
                    }
                    if (!job.has("-usecaseDiscription ")) {
                        model.put("usecaseDescription", "  ");
                    }
                    if (job.has("-RefinesTo ")) {

                        String _parentGoal = job.getString("-RefinesTo ");
                        String parentGoal = _parentGoal.replace("</br>","\",\n\"");
                        parentGoal = parentGoal.substring(0,parentGoal.length()-1);
                        model.put("RefinesTo", parentGoal);
                    }
                    if (!job.has("-RefinesTo ")) {
                        model.put("RefinesTo", "  ");
                    }
                    if (job.has("-RefinedBy ")) {
                        String _subGoal = job.getString("-RefinedBy ");
                        String subGoal = _subGoal.replace("</br>","\",\n\"");
                        subGoal = subGoal.substring(0,subGoal.length()-1);
                        model.put("RefinedBy", subGoal);
                    }
                    if (!job.has("-RefinedBy ")) {
                        model.put("RefinedBy", "  ");
                    }
                    if (job.has("-Obstructs ")) {
                        String _failures = job.getString("-Obstructs ");
                        String failures = _failures.replace("</br>","\",\n\"");
                        failures = failures.substring(0,failures.length()-1);
                        model.put("Obstructs", failures);
                    }
                    if (!job.has("-Obstructs ")) {
                        model.put("Obstructs", "  ");
                    }
                    if (job.has("-Resolves ")) {
                        String _subRequire = job.getString("-Resolves ");
                        String subRequire = _subRequire.replace("</br>","\",\n\"");
                        subRequire = subRequire.substring(0,subRequire.length()-1);
                        model.put("Resolves", subRequire);
                    }
                    if (!job.has("-Resolves ")) {
                        model.put("Resolves", "  ");
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
                    value = value.substring(0,value.length()-1);
                    if (job.has("-Description ")) {
                        String nonFunctionalRule = job.getString("-Description ");
                        nonFunctionalRule = nonFunctionalRule.substring(0,nonFunctionalRule.length()-1);
                        if(nonFunctionalRule.equals("undefined")){
                            model.put("Description", "  ");
                        }
                        else {
                            model.put("Description", nonFunctionalRule);
                        }
                    }
                    if (!job.has("-Description ")) {
                        model.put("Description", "  ");
                    }
                    if (job.has("-RefinesTo ")) {
                        String _refines = job.getString("-RefinesTo ");
                        String refines = _refines.replace("</br>","\",\n\"");
                        refines = refines.substring(0,refines.length()-1);
                        model.put("RefinesToReq", refines);
                    }
                    if (!job.has("-RefinesTo ")) {
                        model.put("RefinesToReq", "  ");
                    }
                    if (job.has("-Agents ")) {
                        String _agents = job.getString("-Agents ");
                        String agents = _agents.replace("</br>","\",\n\"");
                        agents = agents.substring(0,agents.length()-1);
                        model.put("agents", agents);
                    }
                    if (!job.has("-Agents ")) {
                        model.put("agents", "  ");
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
                        value = value.substring(0,value.length()-1);
                        if (job.has("-resourType ")) {
                            String resourceTypes = job.getString("-resourType ");
                            resourceTypes = resourceTypes.substring(0,resourceTypes.length()-1);
                            if(resourceTypes.equals("undefined")){
                                model.put("resourType", "  ");
                            }
                            else {
                                model.put("resourType", resourceTypes);
                            }
                        }
                        if (!job.has("-resourType ")) {
                            model.put("resourType", "  ");
                        }
                        if (job.has("-RelateTo ")) {
                            String _relates = job.getString("-RelateTo ");
                            String relates = _relates.replace("</br>","\",\n\"");
                            relates = relates.substring(0,relates.length()-1);
                            model.put("RelateTo", relates);
                        }
                        if (!job.has("-RelateTo ")) {
                            model.put("RelateTo", "  ");
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
                    value = value.substring(0,value.length()-1);
                    Map<String, Object> model = new HashMap<String, Object>();
                    if (job.has("-gedetail ")) {
                        String detail = job.getString("-gedetail ");
                        detail = detail.substring(0,detail.length()-1);
                        if(detail.equals("undefined")){
                            model.put("detail", "  ");
                        }
                        else {
                            model.put("detail", detail);
                        }
                    }
                    if (!job.has("-gedetail ")) {
                        model.put("detail", "  ");
                    }
                    if (job.has("-Goal ")) {
                        String detail = job.getString("-Goal ");
                        detail = detail.substring(0,detail.length()-1);
                        model.put("Target", detail);
                    }
                    if (!job.has("-Goal ")) {
                        model.put("Target", "  ");
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

                else if (flag.equals("hexagon ")) {
                    if(job.has("-value ")) {
                        String sbOneHex;
                        Map<String, Object> model = new HashMap<String, Object>();
                        String value = job.getString("-value ");
                        value = value.substring(0,value.length()-1);
                        if (job.has("-agentType ")) {
                            String agentTypes = job.getString("-agentType ");
                            agentTypes = agentTypes.substring(0,agentTypes.length()-1);
                            if(agentTypes.equals("undefined")){
                                model.put("agentType", "  ");
                            }
                            else {
                                model.put("agentType", agentTypes);
                            }
                        }
                        if (!job.has("-agentType ")) {
                            model.put("agentType", "  ");
                        }

                        model.put("value", value);
                        Template t = null;
                        if (templateID.equals("md")) {
                            t = configuration.getTemplate("mdftl/agentmd.ftl", "UTF-8");
                        }
                        else if(templateID.equals("rela")){
                            t = configuration.getTemplate("jsonftl/agentjs.ftl", "UTF-8");
                        }
                        else {
                            t = configuration.getTemplate("rstftl/agentrst.ftl", "UTF-8");
                        }
                        indexHex += 1;
                        model.put("index", indexHex);
                        sbOneHex = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                        sbHex += sbOneHex;
                    }
                }

                else if (flag.equals("domain_property ")) {
                    if(job.has("-value ")) {
                        String sbOneDom;
                        Map<String, Object> model = new HashMap<String, Object>();
                        String value = job.getString("-value ");
                        value = value.substring(0,value.length()-1);
                        if (job.has("-domainPropertyRef ")) {
                            String domainPropertyRef = job.getString("-domainPropertyRef ");
                            domainPropertyRef = domainPropertyRef.substring(0,domainPropertyRef.length()-1);
                            if(domainPropertyRef.equals("undefined")){
                                model.put("domainPropertyRef", "  ");
                            }
                            else {
                                model.put("domainPropertyRef", domainPropertyRef);
                            }
                        }
                        if (!job.has("-domainPropertyRef ")) {
                            model.put("domainPropertyRef", "  ");
                        }

                        model.put("value", value);
                        Template t = null;
                        if (templateID.equals("md")) {
                            t = configuration.getTemplate("mdftl/domainpropertymd.ftl", "UTF-8");
                        }
                        else if(templateID.equals("rela")){
                            t = configuration.getTemplate("jsonftl/domainpropertyjs.ftl", "UTF-8");
                        }
                        else {
                            t = configuration.getTemplate("rstftl/domainpropertyrst.ftl", "UTF-8");
                        }
                        indexDom += 1;
                        model.put("index", indexDom);
                        sbOneDom = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                        sbDom += sbOneDom;
                    }
                }

                else if (!flag.equals("ellipse ")){
                    if(job.has("-value ")) {

                        String value = job.getString("-value ");
                        value = value.substring(0,value.length()-1);
                        Map<String, Object> model = new HashMap<String, Object>();
                        if (job.has("-gedetail ")) {
                            String detail = job.getString("-gedetail ");
                            detail = detail.substring(0,detail.length()-1);
                            if(detail.equals("undefined")){
                                model.put("detail","  ");
                            }
                            else {
                                model.put("detail", detail);
                            }
                        }
                        if (!job.has("-gedetail ")) {
                            model.put("detail", "  ");
                        }
                        model.put("value", value);

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
        result = result+sbGoal+sbObs+sbReq+sbRes+sbHex+sbDom;
        if(templateID.equals("rela")){
            for(int i = result.length() - 1; i >= 0; i--){
                if(',' == result.charAt(i)){
                    result = result.substring(0,i) + result.substring(i+1);
                    break;
                }
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
