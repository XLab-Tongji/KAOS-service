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
        String content;Map<String,Object> name=new HashMap<String, Object>();
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
        String sbResi = new String();
        String sbDis = new String();
        String sbOther = new String();
        String sbTest = new String();
        String sbPer = new String();
        int indexReq = 0;
        int indexGoal = 0;
        int indexObs = 0;
        int indexRes = 0;
        int indexHex = 0;
        int indexDom = 0;
        int indexResi = 0;
        int indexDis=0;
        int indexTest=0;
        int indexPer = 0;
        int indexOther=0;
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
                        if (templateID.equals("rela")) {
                            _parentGoal = _parentGoal.substring(0, _parentGoal.length() - 1);
                            _parentGoal = "\"" + _parentGoal + "\"";
                            String parentGoal = _parentGoal.replace("</br>", "\",\n\"");
                            model.put("RefinesTo", parentGoal);
                        } else {
                            String parentGoal = _parentGoal.replace("</br>", " --- ");
                            model.put("RefinesTo", parentGoal);
                        }

                    }
                    if (!job.has("-RefinesTo ")) {
                        model.put("RefinesTo", "");
                    }
                    if (job.has("-RefinedBy ")) {
                        String _subGoal = job.getString("-RefinedBy ");
                        if(templateID.equals("rela")) {
                            _subGoal = _subGoal.substring(0, _subGoal.length() - 1);
                            _subGoal = "\""+ _subGoal + "\"";
                            String subGoal = _subGoal.replace("</br>", "\",\n\"");
                            model.put("RefinedBy", subGoal);
                        }
                        else{
                            String subGoal = _subGoal.replace("</br>", " --- ");
                            model.put("RefinedBy", subGoal);
                        }
                    }
                    if (!job.has("-RefinedBy ")) {
                        model.put("RefinedBy", "");
                    }
                    if (job.has("-Obstructs ")) {
                        String _failures = job.getString("-Obstructs ");
                        if(templateID.equals("rela")) {
                            _failures = _failures.substring(0, _failures.length() - 1);
                            _failures = "\""+ _failures + "\"";
                            String failures = _failures.replace("</br>", "\",\n\"");
                            model.put("Obstructs", failures);
                        }
                        else{
                            String failures = _failures.replace("</br>", " --- ");
                            model.put("Obstructs", failures);
                        }
                    }
                    if (!job.has("-Obstructs ")) {
                        model.put("Obstructs", "");
                    }
                    if (job.has("-Resolves ")) {
                        String _subRequire = job.getString("-Resolves ");
                        if(templateID.equals("rela")) {
                            _subRequire = _subRequire.substring(0, _subRequire.length() - 1);
                            _subRequire = "\"" + _subRequire + "\"";
                            String subRequire = _subRequire.replace("</br>", "\",\n\"");
                            model.put("Resolves", subRequire);
                        }
                        else{
                            String subRequire = _subRequire.replace("</br>", " --- ");
                            model.put("Resolves", subRequire);
                        }
                    }
                    if (!job.has("-Resolves ")) {
                        model.put("Resolves", "");
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
                }
                else if (flag.equals("requirement ")) {
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
                        if(templateID.equals("rela")) {
                            _refines = _refines.substring(0, _refines.length() - 1);
                            _refines = "\"" + _refines + "\"";
                            String refines = _refines.replace("</br>", "\",\n\"");
                            model.put("RefinesToReq", refines);
                        }
                        else{
                            String refines = _refines.replace("</br>", " --- ");
                            model.put("RefinesToReq", refines);
                        }
                    }
                    if (!job.has("-RefinesTo ")) {
                        model.put("RefinesToReq", "");
                    }
                    if (job.has("-Agents ")) {
                        String _agents = job.getString("-Agents ");
                        if(templateID.equals("rela")) {
                            _agents = _agents.substring(0, _agents.length() - 1);
                            _agents = "\"" + _agents + "\"";
                            String agents = _agents.replace("</br>", "\",\n\"");
                            model.put("agents", agents);
                        }
                        else{
                            String agents = _agents.replace("</br>", " --- ");
                            model.put("agents", agents);
                        }
                    }
                    if (!job.has("-Agents ")) {
                        model.put("agents", "");
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
                        value = value.substring(0,value.length()-1); //remove the blank space
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
                            if (templateID.equals("rela")) {
                                _relates = _relates.substring(0, _relates.length() - 1);
                                _relates = "\"" + _relates + "\"";
                                String relates = _relates.replace("</br>", "\",\n\"");
                                model.put("RelateTo", relates);
                            } else {
                                String relates = _relates.replace("</br>", " --- ");
                                model.put("RelateTo", relates);
                            }
                        }
                        if (!job.has("-RelateTo ")) {
                            model.put("RelateTo", "");
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
                    if (job.has("-Obstructs ")) {
                        String _detail = job.getString("-Obstructs ");
                        if(templateID.equals("rela")) {
                            _detail = _detail.substring(0, _detail.length() - 1);
                            _detail = "\"" + _detail + "\"";
                            String detail = _detail.replace("</br>", "\",\n\"");
                            model.put("Target", detail);
                        }
                        else{
                            String detail = _detail.replace("</br>", " --- ");
                            model.put("Target", detail);
                        }
                    }
                    if (!job.has("-Obstructs ")) {
                        model.put("Target", "");
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

                        if (job.has("-domainPropertyDes ")) {
                            String domainPropertyDes = job.getString("-domainPropertyDes ");
                            domainPropertyDes = domainPropertyDes.substring(0,domainPropertyDes.length()-1);
                            if(domainPropertyDes.equals("undefined")){
                                model.put("domainPropertyDes", "  ");
                            }
                            else {
                                model.put("domainPropertyDes", domainPropertyDes);
                            }
                        }
                        if (!job.has("-domainPropertyDes ")) {
                            model.put("domainPropertyDes", "  ");
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
                else if(flag.equals("resilience ")){
                    if(job.has("-value ")) {
                        String sbOneResi;
                        Map<String, Object> model = new HashMap<String, Object>();
                        String value = job.getString("-value ");
                        value = value.substring(0,value.length()-1);
                        if (job.has("-TargetRes ")) {
                            String resilienceRef = job.getString("-TargetRes ");
                            resilienceRef = resilienceRef.substring(0,resilienceRef.length()-1);
                            model.put("TargetRes", resilienceRef);
                        }
                        if (!job.has("-TargetRes ")) {
                            model.put("TargetRes", "  ");
                        }
                        if (job.has("-ObstructsResi ")) {
                            String resilienceRef = job.getString("-ObstructsResi ");
                            resilienceRef = resilienceRef.replace("</br>","");
                            model.put("ObstructsResi", resilienceRef);
                        }
                        if (!job.has("-ObstructsResi ")) {
                            model.put("ObstructsResi", "  ");
                        }
                        if (job.has("-BenchmarkedBy ")) {
                            String resilienceRef = job.getString("-BenchmarkedBy ");
                            resilienceRef = resilienceRef.substring(0,resilienceRef.length()-1);
                            model.put("BenchmarkedBy", resilienceRef);
                        }
                        if (!job.has("-BenchmarkedBy ")) {
                            model.put("BenchmarkedBy", "  ");
                        }

                        if (job.has("-DisruptionTol ")) {
                            String resilienceRef = job.getString("-DisruptionTol ");
                            resilienceRef = resilienceRef.replace("undefined","");
                            model.put("DisruptionTol", resilienceRef);
                        }
                        if (!job.has("-DisruptionTol ")) {
                            model.put("DisruptionTol", "  ");
                        }
                        if (job.has("-DTUnit ")) {
                            String resilienceRef = job.getString("-DTUnit ");
                            resilienceRef = resilienceRef.replace("undefined","");
                            model.put("DTUnit", resilienceRef);
                        }
                        if (!job.has("-DTUnit ")) {
                            model.put("DTUnit", "  ");
                        }
                        if (job.has("-RecoveryTime ")) {
                            String resilienceRef = job.getString("-RecoveryTime ");
                            resilienceRef = resilienceRef.replace("undefined","");
                            model.put("RecoveryTime", resilienceRef);
                        }
                        if (!job.has("-RecoveryTime ")) {
                            model.put("RecoveryTime", "  ");
                        }
                        if (job.has("-QualityLoss ")) {
                            String resilienceRef = job.getString("-QualityLoss ");
                            resilienceRef = resilienceRef.replace("undefined","");
                            model.put("QualityLoss", resilienceRef);
                        }
                        if (!job.has("-QualityLoss ")) {
                            model.put("QualityLoss", "  ");
                        }
                        if (job.has("-QLUnit ")) {
                            String resilienceRef = job.getString("-QLUnit ");
                            resilienceRef = resilienceRef.replace("undefined","");
                            model.put("QLUnit", resilienceRef);
                        }
                        if (!job.has("-QLUnit ")) {
                            model.put("QLUnit", "  ");
                        }
                        model.put("value", value);
                        Template t = null;
                        if (templateID.equals("md")) {
                            t = configuration.getTemplate("mdftl/resiliencemd.ftl", "UTF-8");
                        }
                        else if(templateID.equals("rela")){
                            t = configuration.getTemplate("jsonftl/resiliencejs.ftl", "UTF-8");
                        }
                        else {
                            t = configuration.getTemplate("rstftl/resiliencerst.ftl", "UTF-8");
                        }
                        indexResi += 1;
                        model.put("index", indexResi);
                        sbOneResi = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                        sbResi += sbOneResi;
                    }
                }
                else if(flag.equals("disruption ")){
                    if(job.has("-value ")) {
                        String sbOneDis;
                        Map<String, Object> model = new HashMap<String, Object>();
                        String value = job.getString("-value ");
                        value = value.substring(0,value.length()-1);
                        if (job.has("-description ")) {
                            String disruptionRef = job.getString("-description ");
                            disruptionRef = disruptionRef.replace("undefined","");
                            model.put("description", disruptionRef);
                        }
                        if (!job.has("-description ")) {
                            model.put("description", "  ");
                        }
                        if (job.has("-DisruptionTol ")) {
                            String disruptionRef = job.getString("-DisruptionTol ");
                            disruptionRef = disruptionRef.replace("undefined","");
                            model.put("DisruptionTol", disruptionRef);
                        }
                        if (!job.has("-DisruptionTol ")) {
                            model.put("DisruptionTol", "  ");
                        }
                        if (job.has("-ObstructsDis ")) {
                            String disruptionRef = job.getString("-ObstructsDis ");
                            disruptionRef = disruptionRef.replace("undefined","");
                            disruptionRef = disruptionRef.replace("</br>","");
                            model.put("ObstructsDis", disruptionRef);
                        }
                        if (!job.has("-ObstructsDis ")) {
                            model.put("ObstructsDis", "  ");
                        }
                        if (job.has("-RecoveryTime ")) {
                            String disruptionRef = job.getString("-RecoveryTime ");
                            disruptionRef = disruptionRef.replace("undefined","");
                            model.put("RecoveryTime", disruptionRef);
                        }
                        if (!job.has("-RecoveryTime ")) {
                            model.put("RecoveryTime", "  ");
                        }
                        if (job.has("-QualityLoss ")) {
                            String disruptionRef = job.getString("-QualityLoss ");
                            disruptionRef = disruptionRef.replace("undefined","");
                            model.put("QualityLoss", disruptionRef);
                        }
                        if (!job.has("-QualityLoss ")) {
                            model.put("QualityLoss", "  ");
                        }
                        model.put("value", value);
                        Template t = null;
                        if (templateID.equals("md")) {
                            t = configuration.getTemplate("mdftl/disruptionmd.ftl", "UTF-8");
                        }
                        else if(templateID.equals("rela")){
                            t = configuration.getTemplate("jsonftl/disruptionjs.ftl", "UTF-8");
                        }
                        else {
                            t = configuration.getTemplate("rstftl/disruptionjsrst.ftl", "UTF-8");
                        }
                        indexDis += 1;
                        model.put("index", indexDis);
                        sbOneDis = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                        sbDis += sbOneDis;
                    }
                }
                else if(flag.equals("testcase ")){
                    if(job.has("-value ")) {
                        String sbOneTest;
                        Map<String, Object> model = new HashMap<String, Object>();
                        String value = job.getString("-value ");
                        value = value.substring(0,value.length()-1);
                        if (job.has("-Action ")) {
                            String testCaseRef = job.getString("-Action ");
                            testCaseRef = testCaseRef.substring(0,testCaseRef.length()-1);
                            model.put("Action", testCaseRef);
                        }
                        if (!job.has("-Action ")) {
                            model.put("Action", "  ");
                        }

                        if (job.has("-testGoalname ")) {
                            String testCaseRef = job.getString("-testGoalname ");
                            testCaseRef = testCaseRef.substring(0, testCaseRef.length() - 1);
                            testCaseRef = "\"" + testCaseRef + "\"";
                            testCaseRef = testCaseRef.replace("</br>", "\",\"");
                            model.put("testGoalName", testCaseRef);
                        }
                        if (!job.has("-testGoalname ")) {
                            model.put("testGoalName", "  ");
                        }
                        if (job.has("-testDesc ")) {
                            String testCaseRef = job.getString("-testDesc ");
                            testCaseRef = testCaseRef.substring(0, testCaseRef.length() - 1);
                            testCaseRef = "\"" + testCaseRef + "\"";
                            testCaseRef = testCaseRef.replace("</br>", "\",\"");

                            testCaseRef = testCaseRef.replace("undefined","");
                            model.put("testDesc", testCaseRef);
                        }
                        if (!job.has("-testDesc ")) {
                            model.put("testDesc", "  ");
                        }
                        if (job.has("-testDT ")) {
                            String testCaseRef = job.getString("-testDT ");
                            testCaseRef = testCaseRef.substring(0, testCaseRef.length() - 1);
                            testCaseRef = "\"" + testCaseRef + "\"";
                            testCaseRef = testCaseRef.replace("</br>", "\",\"");

                            testCaseRef = testCaseRef.replace("undefined","");
                            model.put("testDT", testCaseRef);
                        }
                        if (!job.has("-testDT ")) {
                            model.put("testDT", "  ");
                        }
                        if (job.has("-testRT ")) {
                            String testCaseRef = job.getString("-testRT ");
                            testCaseRef = testCaseRef.replace("undefined","");
                            testCaseRef = testCaseRef.substring(0, testCaseRef.length() - 1);
                            testCaseRef = "\"" + testCaseRef + "\"";
                            testCaseRef = testCaseRef.replace("</br>", "\",\"");

                            model.put("testRT", testCaseRef);
                        }
                        if (!job.has("-testRT ")) {
                            model.put("testRT", "  ");
                        }
                        if (job.has("-testQL ")) {
                            String testCaseRef = job.getString("-testQL ");
                            testCaseRef = testCaseRef.replace("undefined","");
                            testCaseRef = testCaseRef.substring(0, testCaseRef.length() - 1);
                            testCaseRef = "\"" + testCaseRef + "\"";
                            testCaseRef = testCaseRef.replace("</br>", "\",\"");

                            model.put("testQL", testCaseRef);
                        }
                        if (!job.has("-testQL ")) {
                            model.put("testQL", "  ");
                        }
                        if (job.has("-testQLUnit ")) {
                            String testCaseRef = job.getString("-testQLUnit ");
                            testCaseRef = testCaseRef.substring(0, testCaseRef.length() - 1);
                            testCaseRef = "\"" + testCaseRef + "\"";
                            testCaseRef = testCaseRef.replace("</br>", "\",\"");

                            testCaseRef = testCaseRef.replace("undefined","");

                            model.put("testQLUnit", testCaseRef);
                        }
                        if (!job.has("-testQLUnit ")) {
                            model.put("testQLUnit", "  ");
                        }
                        if (job.has("-testBench ")) {
                            String testCaseRef = job.getString("-testBench ");
                            testCaseRef = testCaseRef.substring(0, testCaseRef.length() - 1);
                            testCaseRef = "\"" + testCaseRef + "\"";
                            testCaseRef = testCaseRef.replace("</br>", "\",\"");

                            testCaseRef = testCaseRef.replace("undefined","");
                            model.put("testBench", testCaseRef);
                        }
                        if (!job.has("-testBench ")) {
                            model.put("testBench", "  ");
                        }
                        if (job.has("-testTarget ")) {
                            String testCaseRef = job.getString("-testTarget ");
                            testCaseRef = testCaseRef.substring(0, testCaseRef.length() - 1);
                            testCaseRef = "\"" + testCaseRef + "\"";
                            testCaseRef = testCaseRef.replace("</br>", "\",\"");

                            testCaseRef = testCaseRef.replace("undefined","");
                            model.put("testTarget", testCaseRef);
                        }
                        if (!job.has("-testTarget ")) {
                            model.put("testTarget", "  ");
                        }
                        model.put("value", value);
                        Template t = null;
                        if (templateID.equals("md")) {
                            t = configuration.getTemplate("mdftl/disruptionmd.ftl", "UTF-8");
                        }
                        else if(templateID.equals("rela")){
                            t = configuration.getTemplate("jsonftl/testcasejs.ftl", "UTF-8");
                        }
                        else {
                            t = configuration.getTemplate("rstftl/disruptionjsrst.ftl", "UTF-8");
                        }
                        indexTest += 1;
                        model.put("index", indexTest);
                        sbOneTest = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                        sbTest += sbOneTest;
                    }
                }
                else if(flag.equals("performancebenchmark ")){
                    if(job.has("-value ")) {
                        String sbOnePer;
                        Map<String, Object> model = new HashMap<String, Object>();
                        String value = job.getString("-value ");
                        value = value.substring(0,value.length()-1);
                        model.put("value", value);
                        if (job.has("-PerformanceValue ")) {
                            String perRef = job.getString("-PerformanceValue ");
                            perRef.replace("undefined","");
                            perRef = perRef.substring(0,perRef.length()-1);
                            model.put("PerformanceValue", perRef);
                        }
                        if (!job.has("-PerformanceValue ")) {
                            model.put("PerformanceValue", "  ");
                        }

                        if (job.has("-PerformanceTarget ")) {
                            String perRef = job.getString("-PerformanceTarget ");
                            perRef.replace("undefined","");
                            perRef = perRef.substring(0,perRef.length()-1);
                            model.put("PerformanceTarget", perRef);
                        }
                        if (!job.has("-PerformanceTarget ")) {
                            model.put("PerformanceTarget", "  ");
                        }

                        if (job.has("-Unit ")) {
                            String perRef = job.getString("-Unit ");
                            perRef.replace("undefined","");
                            perRef = perRef.substring(0,perRef.length()-1);
                            model.put("Unit", perRef);
                        }
                        if (!job.has("-Unit ")) {
                            model.put("Unit", "  ");
                        }

                        Template t = null;
                        if (templateID.equals("md")) {
                            t = configuration.getTemplate("mdftl/disruptionmd.ftl", "UTF-8");
                        }
                        else if(templateID.equals("rela")){
                            t = configuration.getTemplate("jsonftl/performancebenchmarkjs.ftl", "UTF-8");
                        }
                        else {
                            t = configuration.getTemplate("rstftl/disruptionjsrst.ftl", "UTF-8");
                        }
                        indexPer+= 1;
                        model.put("index", indexPer);
                        sbOnePer = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                        sbPer += sbOnePer;
                    }
                }
                else if (!flag.equals("ellipse ")){
                    if(job.has("-value ")) {
                        String sbOneOther;
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
                        indexOther += 1;
                        model.put("value", value);
                        model.put("index", indexOther);
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
                        sbOneOther = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                        sbOther+=sbOneOther;
                    }
                    //System.out.println(content);
                }
                //result += content;
            }
            else{

            }
        }
        //System.out.println(sbReq);
        result = result+sbGoal+sbObs+sbReq+sbRes+sbHex+sbDom+sbDis+sbResi+sbTest+sbPer+sbOther;
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
