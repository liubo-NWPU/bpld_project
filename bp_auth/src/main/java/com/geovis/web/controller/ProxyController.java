package com.geovis.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by CodeGenerator on 2018/05/07.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/proxy")
public class ProxyController {
    @Autowired
    private RestTemplate restTemplate;

//    @Value("${user.userServicePath}")
//    private String userServicePath;

    @GetMapping("/invok")
    public String find1() {
        System.out.println("12345678");
        return "12345665";
    }

    @GetMapping("/invok2")
    private JSON find2() {
        JSONObject js = JSONObject.parseObject("{\"a\":\"123\",\"b\":\"465\"}");
        return js;
    }

    @GetMapping("/invok3")
    private String find3() {
        String json =  this.restTemplate.getForObject("https://api.weixin.qq.com/sns/jscode2session?appid=wxce9d20096f705767&secret=f4018fb09b98d4fdbc252b93cce12561&grant_type=authorization_code&js_code=021ddMD51RK6FL1MC6F51oXYD51ddMDO", String.class);
        JSONObject js = JSONObject.parseObject(json);
        return json;
    }
}
