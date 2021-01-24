package com.rrk.order.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.rrk.common.modules.order.dto.OrderDto;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.order.fegin.ProductFeginClient;
import com.rrk.order.service.ITbOrderService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private ProductFeginClient productFeginClient;

    @Autowired
    private ITbOrderService orderService;

    /**
     * 获取sku的信息5463858
     */
    @org.junit.Test
    public void test01() {
        Long skuId = 5463858L;
        TbSku tbSku = productFeginClient.getSkuById(skuId);
        System.out.println("获取商品微服务的商品sku的数据：" + tbSku);
    }

//    @org.junit.Test
//    public void test02() throws InterruptedException {
//        OrderDto orderDto = new OrderDto();
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 10L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024));
//        for (int i = 0; i < 20; i++) {
//            executor.execute(() -> {
//                    Integer count = orderService.addOrder(orderDto);
//                    if (count <= 0) {
//                        System.out.println("不好意思卖完了" + count+Thread.currentThread().getName());
//                    } else {
//                        System.out.println("线程名字：" + Thread.currentThread().getName()+"剩余库存："+count);
//                    }
//            });
//        }
//    }

    /**
     * 测试下单
     */
    @org.junit.Test
    public void test03() {
        Long skuId = 5463858L;
        OrderDto orderDto = new OrderDto();
        // orderDto.setOrderNo("35442525");
        orderDto.setAddressId(1L);
        orderDto.setSkuId(skuId);
        orderDto.setNum(1);
        Long userId = 19L;
        Integer integer = orderService.addOrder(orderDto, userId);
        if (integer > 0) {
            System.out.println("恭喜您下单成功：" + orderDto);
        } else {
            System.out.println("抱歉。下单失败了：" + orderDto);
        }
    }

    public static final String ACCESS_SECRENT = "7c10190e-2be9-42d5-b12b-805b780a5b3d";
    public static final String KEY = "6888235097808504333";
    public static final String DOUYIN_URL = "https://openapi-fxg.jinritemai.com/order/list?";
    public static final String METHOD = "order.list";
    private static final String DOUDIAN_CACHE_NAME = "doudianCache";
    private static final String DOUDIAN_TOKEN_KEY = "doudian_access_token";



    /**
     * 测试抖音拉取订单列表
     */
    @org.junit.Test
    public void test06() throws UnsupportedEncodingException {


  
        Map<String,String> map = new TreeMap<>();
        map.put("start_time","2020/10/08 00:00:00");
        map.put("end_time","2020/10/09 00:00:00");
        map.put("is_desc","1");
        map.put("page","0");
        map.put("size","10");
        map.put("order_by","create_time");
        String paramJson = JSONObject.toJSONString(map);
        String accessToken = getAccessToken();
        String url = DOUYIN_URL+"app_key="+KEY+"&access_token="+accessToken+"&method="+METHOD
                +"&";
        String paramJson2 = URLEncoder.encode(paramJson, "UTF-8");
        Date now = new Date();
        String dateStr = DateUtil.format(now, "yyyy-MM-dd HH:mm:ss");
        String dateStr2 = URLEncoder.encode(dateStr, "UTF-8");
        String sign = fetchSign("order.list", paramJson, dateStr);
        String param = "param_json="+paramJson2+"&timestamp="+dateStr2+"&v=2"+"&sign="+sign;
        String response = HttpUtil.get(url + param, 20000);
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject.getIntValue("err_no") == 0) {
             jsonObject.getJSONObject("data");
        }
        System.out.println("获取的结果是："+jsonObject);


    }

    public static String fetchSign(String methodName, String paramJson, String timeStamp) {
        String requestStr = ACCESS_SECRENT + "app_key" + KEY + "method" + methodName + "param_json" + paramJson + "timestamp"
                + timeStamp + "v2" + ACCESS_SECRENT;
        return paramToMD5(requestStr);
    }

    public static String paramToMD5(String requestStr) {
        byte[] secretBytes;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    requestStr.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        StringBuilder md5code = new StringBuilder(new BigInteger(1, secretBytes).toString(16));
        while (md5code.length() < 32) {
            md5code.insert(0, "0");
        }
        return md5code.toString();
    }

    /**
     * 获取access_token
     *
     */
    private static final String DOUDIAN_URL = "https://openapi-fxg.jinritemai.com";

    public static String  getAccessToken(){
        String url = DOUDIAN_URL + "/oauth2/access_token" + "?app_id=" + KEY + "&app_secret="
                + ACCESS_SECRENT + "&grant_type=authorization_self";
        String response = HttpUtil.get(url, 20000);
        if (StrUtil.isBlank(response)) {
            for (int i = 0; i < 3; i++) {
                response = HttpUtil.get(url, 20000);
                if (StrUtil.isNotBlank(response)) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject.getIntValue("err_no") == 0) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (null != data) {
                //用于调用API的access_token
                String accessToken = data.getString("access_token");
                if (StrUtil.isNotBlank(accessToken)) {
                   return accessToken;
                }
            }
        }
        return null;
    }

    /**
     * 拉取抖音商品信息
     */
    @org.junit.Test
    public void test08() throws UnsupportedEncodingException {
        Map<String,String> map = new TreeMap<>();
        map.put("page","0");
        map.put("size","10");
        String paramJson = JSONObject.toJSONString(map);
        String accessToken = getAccessToken();
        String url = "https://openapi-fxg.jinritemai.com/product/list?";
        String proUrl = url +"app_key="+KEY+"&access_token="+accessToken+"&method="+"product.list"
                +"&";
        String paramJson2 = URLEncoder.encode(paramJson, "UTF-8");
        Date now = new Date();
        String dateStr = DateUtil.format(now, "yyyy-MM-dd HH:mm:ss");
        String dateStr2 = URLEncoder.encode(dateStr, "UTF-8");
        String sign = fetchSign("product.list", paramJson, dateStr);
        String param = "param_json="+paramJson2+"&timestamp="+dateStr2+"&v=2"+"&sign="+sign;
        String response = HttpUtil.get(proUrl + param, 20000);
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject.getIntValue("err_no") == 0) {
            jsonObject.getJSONObject("data");
        }
        System.out.println("获取的结果是："+jsonObject);

    }

    /**
     * 获取抖音商品的详情
     */
//    @org.junit.Test
//    public

}
