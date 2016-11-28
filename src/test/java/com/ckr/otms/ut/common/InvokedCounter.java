package com.ckr.otms.ut.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/19.
 */
public class InvokedCounter {

    private Map<String, Integer> involedCountNumMap = new HashMap<>();

    public void countInvoke(String methodName){
        Integer count = getInvokeCount(methodName);
        count++;

        involedCountNumMap.put(methodName, count);
    }


    public Integer getInvokeCount(String methodName){

        Integer count = involedCountNumMap.get(methodName);

        if(count == null){
            count = 0;
        }

        return count;

    }
}
