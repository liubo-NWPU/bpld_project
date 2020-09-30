package com.geovis.core.util;

import com.geovis.web.domain.system.ResourcePower;
import com.geovis.web.domain.system.SysUser;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class BeanUtil {

    public static final String ADD = "ADD";
    public static final String REMOVE = "REMOVE";
    public static final String NORMAL = "NORMAL";

    //创建对象时设置用户信息
    public static void setCreateUser(HttpServletRequest request, Object obj){
        try {
            SysUser currentUser = SystemCacheUtil.getUserByRequest(request);

            ReflectUtil.setFieldValue(obj, "createBy", currentUser.getId());
            ReflectUtil.setFieldValue(obj, "creater", currentUser.getName());
            ReflectUtil.setFieldValue(obj, "createDate", new Date());
        }catch(Exception e){
            ReflectUtil.setFieldValue(obj, "createBy", "-1");
            ReflectUtil.setFieldValue(obj, "creater", "-1");
            ReflectUtil.setFieldValue(obj, "createDate", new Date());
        }
    }

    //创建对象时设置用户信息
    public static void setUpdateUser(HttpServletRequest request, Object obj){
        try {
            SysUser currentUser = SystemCacheUtil.getUserByRequest(request);

            ReflectUtil.setFieldValue(obj, "updateBy", currentUser.getId());
            ReflectUtil.setFieldValue(obj, "updater", currentUser.getName());
            ReflectUtil.setFieldValue(obj, "updateDate", new Date());
        }catch(Exception e){
            ReflectUtil.setFieldValue(obj, "updateBy", "-1");
            ReflectUtil.setFieldValue(obj, "updater", "-1");
            ReflectUtil.setFieldValue(obj, "updateDate", new Date());
        }
    }

    //创建对象时设置用户信息
    public static void setCreateUser(SysUser sysUser, Object obj){
        String createBy="0";
        if(sysUser!=null){
            if(sysUser.getId()!=null){
                String userId=sysUser.getId();
              if(userId.length()>0&&!userId.equals("null")){
                  createBy=userId;
              }
            }

        }
        ReflectUtil.setFieldValue(obj ,"createBy", createBy);
        String creater="系统";
        if(sysUser!=null){
            if(sysUser.getName()!=null){
                String createrName=sysUser.getName();
                if(createrName.length()>0&&!createrName.equals("null")){
                    creater=createrName;
                }
            }

        }
        ReflectUtil.setFieldValue(obj ,"creater",creater);
        ReflectUtil.setFieldValue(obj ,"createDate", new Date());
    }

    //创建对象时设置用户信息
    public static void setUpdateUser(SysUser sysUser, Object obj){
        String updateBy="0";
        if(sysUser!=null){
            if(sysUser.getId()!=null){
                String userId=sysUser.getId();
                if(userId.length()>0&&!userId.equals("null")){
                    updateBy=userId;
                }
            }

        }
        ReflectUtil.setFieldValue(obj ,"updateBy", updateBy);
        String updater="系统";
        if(sysUser!=null){
            if(sysUser.getName()!=null){
                String createrName=sysUser.getName();
                if(createrName.length()>0&&!createrName.equals("null")){
                    updater=createrName;
                }
            }

        }
        ReflectUtil.setFieldValue(obj ,"updater",updater);
        ReflectUtil.setFieldValue(obj ,"updateDate", new Date());
    }

   /**
     * 比较原List与现List的差异，返回Map
     * Map的三个KEY值：ADD(新增)\REMOVE（删除）\NORMAL（相同）
     * @param compareList 需要比较的list
     * @param currList 当前的list
     * @return
     */
    public static Map diffList(Set<String> compareList, Set<String> currList){

        if(CollectionUtils.isEmpty(compareList) && CollectionUtils.isEmpty(currList)){
            return null;
        }

        Map<String,Set<String>> result = new HashMap<String,Set<String>>();

        result.put(ADD, new HashSet<String>());
        result.put(REMOVE, new HashSet<String>());
        result.put(NORMAL, new HashSet<String>());

        if(CollectionUtils.isEmpty(compareList)){
            result.put(REMOVE,currList);
        } else if(CollectionUtils.isEmpty(currList)){
            result.put(ADD,compareList);
        } else {
            for(String comStr : compareList){
                if(currList.contains(comStr)){
                    result.get(NORMAL).add(comStr);
                } else {
                    result.get(ADD).add(comStr);
                }
            }

            for(String currStr : currList){
                if(compareList.contains(currStr)){
                    result.get(NORMAL).add(currStr);
                } else {
                    result.get(REMOVE).add(currStr);
                }
            }
        }
        return result;
    }


    public static Map<String,Set<ResourcePower>> diffListObject(Set<ResourcePower> compareList, Set<ResourcePower> currList){

        if(CollectionUtils.isEmpty(compareList) && CollectionUtils.isEmpty(currList)){
            return null;
        }

        Map<String,Set<ResourcePower>> result = new HashMap<String,Set<ResourcePower>>();

        result.put(ADD, new HashSet<ResourcePower>());
        result.put(REMOVE, new HashSet<ResourcePower>());
        result.put(NORMAL, new HashSet<ResourcePower>());

        if(CollectionUtils.isEmpty(compareList)){
            result.put(REMOVE,currList);
        } else if(CollectionUtils.isEmpty(currList)){
            result.put(ADD,compareList);
        } else {
            for(ResourcePower comStr : compareList){
                if(currList.contains(comStr)){
                    result.get(NORMAL).add(comStr);
                } else {
                    result.get(ADD).add(comStr);
                }
            }

            for(ResourcePower currStr : currList){
                if(compareList.contains(currStr)){
                    result.get(NORMAL).add(currStr);
                } else {
                    result.get(REMOVE).add(currStr);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
//        Set<String> compareList = new HashSet<String>();
//        compareList.add("1");
//        compareList.add("2");
//        compareList.add("3");
//        Set<String> currList = new HashSet<String>();
//        currList.add("1");
//        currList.add("2");
//        currList.add("4");
//        Map map = diffList(compareList,currList);
//        System.out.println(map);
//        List<ResourcePower>compareList=new ArrayList<ResourcePower>();
//        //Set<ResourcePower> compareList = new HashSet<ResourcePower>();
//        ResourcePower  resourcePower=new ResourcePower();
//        resourcePower.setPowerId("123");
//        resourcePower.setResourceId("0000");
//        compareList.add(resourcePower);
//
//        ResourcePower  resourcePower1=new ResourcePower();
//        resourcePower1.setPowerId("123");
//        resourcePower1.setResourceId("0000");
//        System.out.println(resourcePower.equals(resourcePower1));
//        System.out.println(compareList.contains(resourcePower1));
    }



}
