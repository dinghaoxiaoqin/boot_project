package com.rrk.manage.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.common.modules.product.entity.TbSpu;
import com.rrk.manage.config.DouyinProperties;
import com.rrk.manage.service.ITbSkuService;
import com.rrk.manage.service.ITbSpuService;
import com.rrk.manage.utils.HttpUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    private final static String PRODUCT_URL = "https://search.jd.com/Search?keyword=以纯&wq=以纯&s=1&click=0&page=1";

    @Autowired
    private DouyinProperties douyinProperties;
    @Autowired
    private ITbSkuService skuService;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ITbSpuService spuService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.Test
    public void  test(){

        System.out.println("url:"+douyinProperties.getUrl());
        System.out.println("key:"+douyinProperties.getKey());
        System.out.println("secret:"+douyinProperties.getAppSecrent());
    }

    @org.junit.Test
    public void test01() throws IOException {
//        httpUtils.("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
//        String html = this.httpUtils.doGetHtml(PRODUCT_URL + 0);
//
//        Document doc = Jsoup.parse(html);
//
//        Elements spuEles = doc.select("div#J_goodsList > ul > li");
//
//        Element element = spuEles.get(0);
//        System.out.println("获取的element:"+element);
//
//        Elements skuEles = element.select("li.ps-item");
//        long skuId = Long.parseLong(skuEles.get(0).select("[data-sku]").attr("data-sku"));
//        String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + skuId);
//        double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
//        System.out.println("获取的价格："+price);
//        String itemUrl = "https://item.jd.com/"+skuId+".html";
//        String itemInfo = this.httpUtils.doGetHtml(itemUrl);
//        System.out.println("itemInfo"+itemInfo);
//        String title = Jsoup.parse(itemInfo).select("div.sku-name").text();
//        System.out.println("获取的标题："+title);
      //  PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
       // cm.setMaxTotal(100);
       // cm.setDefaultMaxPerRoute(10);
      //  CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
       // HttpGet get = new HttpGet(PRODUCT_URL +1);
       // get.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0");
       // CloseableHttpResponse response = httpClient.execute(get);
        //3、接收服务端响应html
        //HttpEntity entity = response.getEntity();
       // String html = EntityUtils.toString(entity, "utf-8");
        //4、使用Jsoup解析html
        //4、使用Jsoup解析html
        //httpUtils.doGetHtml()
      //  Document document = Jsoup.parse(html);
        String html1 = this.httpUtils.doGetHtml(PRODUCT_URL+1);
        Document doc = Jsoup.parse(html1);
       // Elements spuEles1 = doc.select("div#J_goodsList > ul > li");
      //  Element spuEle1 = spuEles1.get(0);
       // String attr1 = spuEle1.attr("data-spu");
       // long spuId1 = Long.parseLong(attr1.equals("") ? "0" : attr1);
       // System.out.println("获的spuId:"+spuId1);
      //  Element ele = spuEles1.get(0);
               // Elements skuEles = ele.select("li.ps-item");
        //long skuId = Long.parseLong(skuEles.get(0).select("[data-sku]").attr("data-sku"));
      //  System.out.println("获取的skuId："+skuId);

//        Elements spuEles = document.select("div#J_goodsList > ul > li");
//        Element spuEle = spuEles.get(0);
//        String attr = spuEle.attr("data-spu");
//        long spuId = Long.parseLong(attr.equals("") ? "0" : attr);
//        System.out.println("获取到的spuId:"+spuId);
        //解析商品列表
       // Elements elements = document.select("li.gl-item");
        Elements spuEles = doc.select("div#J_goodsList > ul > li");
       // System.out.println("获取的："+spuEles);
        Element spuEle = spuEles.get(0);
        String attr = spuEle.attr("data-spu");
        long spu = Long.parseLong(attr.equals("")?"0":attr);
      //  System.out.println("获取到的spu:"+spu);
        //sku
        Elements skuEles = spuEle.select("li.ps-item");
        Element skuEle = skuEles.get(0);
        long sku = Long.parseLong(skuEle.select("[data-sku]").attr("data-sku"));
        System.out.println("获取的sku:"+sku);
        //title
       // String title = element.select("div.p-name em").text();
        String itemUrl = "https://item.jd.com/"+sku+".html";
        String itemInfo = this.httpUtils.doGetHtml(itemUrl);
        String title = Jsoup.parse(itemInfo).select("div.sku-name").text();
        System.out.println("获取的title:"+title);
        //price
        //String price = element.select("div.p-price i").text();
       // System.out.println("获取的价格："+price);
        //图片
       // String imgUrl = element.select("div.p-img img").attr("src");
      //  System.out.println("获取的url:"+imgUrl);
       // String imageName = downloadImage(imgUrl);
        //商品的url
       // String itemUrl = element.select("div.p-img > a").attr("href");

    }

    @org.junit.Test
    public void  test03() throws Exception {

        String input = "周大福足金黄金戒指";
// 需要爬取商品信息的网站地址
        String url = "https://list.tmall.com/search_product.htm?q=" + input;
// 动态模拟请求数据
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
// 模拟浏览器浏览（user-agent的值可以通过浏览器浏览，查看发出请求的头文件获取）
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        CloseableHttpResponse response = httpclient.execute(httpGet);
// 获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        try {
            HttpEntity entity = response.getEntity();
            // 如果状态响应码为200，则获取html实体内容或者json文件
            if(statusCode == 200){
                String html = EntityUtils.toString(entity, Consts.UTF_8);
                // 提取HTML得到商品信息结果
                Document doc = null;
                // doc获取整个页面的所有数据
                doc = Jsoup.parse(html);
                //输出doc可以看到所获取到的页面源代码
//      System.out.println(doc);
                // 通过浏览器查看商品页面的源代码，找到信息所在的div标签，再对其进行一步一步地解析
                Elements ulList = doc.select("div[class='view grid-nosku']");
                //System.out.println("获取的商品列表："+ulList);
                Elements liList = ulList.select("div[class='product']");

              //  System.out.println("获的sku"+liList);
//                // 循环liList的数据（具体获取的数据值还得看doc的页面源代码来获取，可能稍有变动）
                 List<TbSku> tbSkus = new ArrayList<>();
                for (Element item : liList) {

                    // 商品ID
                    String id = item.select("div[class='product']").select("p[class='productStatus']").select("span[class='ww-light ww-small m_wangwang J_WangWang']").attr("data-item");
                   // System.out.println("获取的id:"+Convert.toLong(id));
                    TbSku one = skuService.getOne(new QueryWrapper<TbSku>().eq("id", Convert.toLong(id)));
                    if (ObjectUtil.isNull(one)) {
                        TbSku sku = new TbSku();
                        sku.setId(Convert.toLong(id));
                       // System.out.println("商品ID："+id);
                        // 商品名称
                        String name = item.select("p[class='productTitle']").select("a").attr("title");
                      //  System.out.println("商品名称："+name);
                        // 商品价格
                        String price = item.select("p[class='productPrice']").select("em").attr("title");
                       // System.out.println("商品价格："+price);
                        // 商品网址
                        String goodsUrl = item.select("p[class='productTitle']").select("a").attr("href");
                        //System.out.println("商品网址："+goodsUrl);
                        // 商品图片网址
                        String imgUrl = item.select("div[class='productImg-wrap']").select("a").select("img").attr("data-ks-lazyload");
                        //System.out.println("商品图片网址："+imgUrl);
                       // System.out.println("------------------------------------");
                        sku.setTitle(name);
                        sku.setEnable(1);
                        sku.setIsVip(1);
                        sku.setStock(1000);
                        sku.setImages(imgUrl);
                        sku.setCreateTime(new Date());
                        sku.setSalePrice(Convert.toBigDecimal(price));
                        sku.setSaleCount(0);
                        sku.setSpuId(0L);
                        sku.setSalePrice(Convert.toBigDecimal(Convert.toDouble(price)*0.8));
                        tbSkus.add(sku);
                    }

                }
                if (CollUtil.isNotEmpty(tbSkus)) {
                    TbSpu spu = new TbSpu();
                    spu.setBrandId(325474L);
                    spu.setTitle("周大福足金黄金戒指");
                    spu.setValid(1);
                    spu.setCreateTime(new Date());
                    spu.setCid1(966L);
                    spu.setCid2(968L);
                    spu.setSpuDescrition("周大福（CHOW TAI FOOK）礼物 心心相守 足金黄金戒指");
                    spu.setSaleable(1);
                    spu.setSubTitle("周大福足金黄金戒指");
                    spuService.save(spu);
                    for (TbSku sku : tbSkus) {
                        sku.setSpuId(spu.getId());
                        skuService.save(sku);
                    }
                }

               // skuService.saveBatch(tbSkus);
                // 消耗掉实体
               // EntityUtils.consume(response.getEntity());
            } else {
                // 消耗掉实体
              //  EntityUtils.consume(response.getEntity());
            }
        } finally {
            response.close();
        }

    }

    @org.junit.Test
    public void test05(){
        List<TbSpu> tbSpus = new ArrayList<>();
       List<TbSku> list =  skuService.list(new QueryWrapper<TbSku>().like("title","Redmi 9"));
       TbSpu spu = new TbSpu();
       spu.setBrandId(18374L);
       spu.setTitle("Redmi9");
       spu.setValid(1);
       spu.setCreateTime(new Date());
       spu.setCid1(76L);
       spu.setCid2(76L);
       //spu.setCid3(812L);
       //spu.setCid3(890L);
       spu.setSpuDescrition("Redmi9");
       spu.setSaleable(1);
       spu.setSubTitle("Redmi 9 5020mAh大电量 1080P全高清大屏 大字体大音量大内存 全场景AI四摄 高性能游戏芯 4GB+64GB 藕荷粉 游戏智能手机 小米 红米");
       spuService.save(spu);
        for (TbSku sku : list) {
            sku.setSpuId(spu.getId());
            skuService.update(sku,new QueryWrapper<TbSku>().eq("id",sku.getId()));
        }
    }
}
