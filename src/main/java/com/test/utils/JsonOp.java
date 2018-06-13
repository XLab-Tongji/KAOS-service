package com.test.utils;

import net.sf.json.JSONArray;

public class JsonOp {
    /**
     * json工具类，解析json数据
     * @param jsonArray
     * @param currentGoalID
     * @param flag
     */
    public void resolveJsonString(JSONArray jsonArray, String currentGoalID, String flag){
        for(int i = 0;i < jsonArray.size();i++){
            String[] results;
            if(jsonArray.getJSONObject(i).getString("-flag ").equals("goal ")){

            }
        }
    }
}
