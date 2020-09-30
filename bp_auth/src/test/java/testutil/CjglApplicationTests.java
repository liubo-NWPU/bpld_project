package testutil;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;


public class CjglApplicationTests {
        public static void main(String[] args) {
            try {


            RestTemplate template = new RestTemplate();
            String url = "http://192.168.31.38:8080/plugin/selectByLists";
            // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            JSONObject postData = new JSONObject();
            postData.put("name", "STS");
            } catch (Exception e) {

            }
            // 1、使用postForObject请求接口
//            String result = template.postForObject(url, postData, String.class);
//            JSONObject jsonObject=JSONObject.parseObject(result);


            // 2、使用postForEntity请求接口
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(postData,headers);
//            ResponseEntity<String> response2 = template.postForEntity(url, httpEntity, String.class);
//            System.out.println("result2====================" + response2.getBody());
//
//            // 3、使用exchange请求接口
//            ResponseEntity<String> response3 = template.exchange(url, HttpMethod.POST, httpEntity, String.class);
//            System.out.println("result3====================" + response3.getBody());


        }

}
