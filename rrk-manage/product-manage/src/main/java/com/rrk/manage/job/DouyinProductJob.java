package com.rrk.manage.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rrk.common.modules.product.entity.TbSku;
import com.rrk.common.modules.product.entity.TbSpu;
import com.rrk.manage.dto.DouyinProductDto;
import com.rrk.manage.service.ITbSkuService;
import com.rrk.manage.service.ITbSpuService;
import com.rrk.manage.utils.HttpUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author dh
 * @date 2020-11-6
 * 拉取抖音商品的任务调度
 */
@Component
// @JobHandler(value = "DouyinProductJob")
@Slf4j
public class DouyinProductJob extends IJobHandler {

    @Autowired
    private ITbSpuService spuService;

    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private ITbSkuService skuService;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final static String PRODUCT_URL = "https://search.jd.com/Search?keyword=稻草人背包&wq=稻草人背包&s=104&click=1&page=";
//
    private final static String PRODUCT_METHOD = "product.list";

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("拉取京东商品列表的定时器执行了-----------------------");
        //PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
       // cm.setMaxTotal(100);
       // cm.setDefaultMaxPerRoute(10);
      //  CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
       // Integer i =
        Integer i = new Random(0).nextInt(10);
        Integer j = 20+new Random().nextInt(50);
        for (i = 1; i < j; i = i +1) {
           // HttpGet get = new HttpGet(PRODUCT_URL + i);
         //   get.setHeader("User-Agent","Mozilla/5.0");
          //  get.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0");
          //  CloseableHttpResponse response = httpClient.execute(get);
          //  HttpEntity entity = response.getEntity();
          //  String html = EntityUtils.toString(entity, "utf-8");
            String html = httpUtils.doGetHtml(PRODUCT_URL+1);
           // System.out.println("html+"+html);
         //   String html = this.httpUtils.doGetHtml(PRODUCT_URL + i);
            //System.out.println("获取的数据："+html);
            //  解析页面，获取商品数据并存储
            if (html != null) {
                this.parse(html);
                //System.out.println("获取京东的数据："+spuEles);
            }

        }
        //对获取的数据进行处理
        log.info("拉取京东商品列表的执行器执行完成--------------------");
        return ReturnT.SUCCESS;
    }

    private void parse(String html) throws Exception {
        //  解析HTML获取Document
        Document doc = Jsoup.parse(html);
        Elements spuEles = doc.select("div#J_goodsList > ul > li");
        //TbSku sku = null;
        List<TbSku> skus = new ArrayList<>();
        List<TbSpu> spuList = new ArrayList<>();
            for (Element spuEle : spuEles) {
                String attr = spuEle.attr("data-spu");
                long spuId = Long.parseLong(attr.equals("")?"0":attr);
                //  获取sku信息
                Elements skuEles = spuEle.select("li.ps-item");
                for (Element skuEle : skuEles) {
                    long skuId = Long.parseLong(skuEle.select("[data-sku]").attr("data-sku"));

                    //  商品图片
                    String picUrl = skuEle.select("img[data-sku]").first().attr("data-lazy-img");
                    //	图片路径可能会为空的情况
                    if(!StrUtil.isNotBlank(picUrl)){
                        picUrl =skuEle.select("img[data-sku]").first().attr("data-lazy-img-slave");
                    }
                    picUrl ="https:"+picUrl.replace("/n7/","/n1/");	//	替换图片格式
                    String picName = this.httpUtils.doGetImage(picUrl);
                    //  商品价格
                    String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + skuId);
                    double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                    //图片
                    String itemUrl = "https://item.jd.com/"+skuId+".html";
                    //  商品标题
                    String itemInfo = this.httpUtils.doGetHtml(itemUrl);
                    String title = Jsoup.parse(itemInfo).select("div.sku-name").text();
                   // item.setTitle(title);
                    //String imageName = downloadImage(imgUrl);
                    //  获取sku
                  TbSku  sku = skuService.getOne(new QueryWrapper<TbSku>().eq("id", skuId));
                    if (ObjectUtil.isNull(sku)) {
                        //if (spuId != 0L ) {
                            //TbSpu one = spuService.getOne(new QueryWrapper<TbSpu>().eq("id", spuId));
//                            if (ObjectUtil.isNull(one)) {
//                                one= new TbSpu();
//                                one.setId(spuId);
//                                one.setTitle(title);
//                                one.setSubTitle(title);
//                                one.setValid(1);
//                                one.setCreateTime(new Date());
//                                one.setBrandId(325426L);
//                                one.setSaleable(1);
//                                one.setSpuDescrition(title);
//                                one.setCreateTime(new Date());
//                                spuList.add(one);
//                            }
                            sku = new TbSku();
                            sku.setId(skuId);
                            sku.setSpuId(0L);
                            sku.setImages(picUrl );
                            //  商品价格
                            sku.setPrice(Convert.toBigDecimal(price));
                            sku.setSalePrice(Convert.toBigDecimal(price*0.8));
                            //  商品标题
                            // spuTitle = title.split(" ")[0];
                            sku.setTitle(title);
                            sku.setCreateTime(new Date());
                            sku.setSaleCount(0);
                            sku.setStock(1000);
                            sku.setIsVip(0);
                            sku.setEnable(1);
                            skus.add(sku);
                        //}
                    }
                }

            }
        if (CollUtil.isNotEmpty(skus)) {
            TbSpu spu = new TbSpu();
            spu.setBrandId(325463L);
            spu.setTitle("稻草人双肩背包");
            spu.setValid(1);
            spu.setCreateTime(new Date());
            spu.setCid1(1187L);
            spu.setCid2(1192L);
            spu.setSpuDescrition("稻草人双肩背包");
            spu.setSaleable(1);
            spu.setSubTitle("稻草人（MEXICAN）双肩背包女韩版潮女包背包");
            spuService.save(spu);
            for (TbSku sku : skus) {
                sku.setSpuId(spu.getId());
                skuService.save(sku);
            }
        }

        System.out.println("获的数据："+skus);
       // skuService.saveBatch(skus);
       // spuService.saveBatch(spuList);
    }
        //


//    private String downloadImage(String imageUrl) throws Exception {
//        //创建一个HttpClient对象
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//        cm.setMaxTotal(100);
//        cm.setDefaultMaxPerRoute(10);
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
//        //创建一个HttpGet对象
//        HttpGet get = new HttpGet("https:" + imageUrl);
//        get.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0");
//        //发送请求
//        CloseableHttpResponse response = httpClient.execute(get);
//        //接收服务端响应的内容。
//        HttpEntity entity = response.getEntity();
//        //需要截取扩展名
//        String extName = imageUrl.substring(imageUrl.lastIndexOf("."));
//        //需要生成文件名。可以使用uuid生成文件名。
//        String fileName = UUID.randomUUID() + extName;
//        //D:\temp\term331\images
//        //创建一个文件输出流，把文件保存到磁盘
//        FileOutputStream outputStream = new FileOutputStream("D:\\image\\" + fileName);
//        //接收流，把内容保存到磁盘。
//        entity.writeTo(outputStream);
//        //关闭流
//        outputStream.close();
//        //关闭Response对象
//        response.close();
//        return fileName;
//    }
    private void saveProducts(List<DouyinProductDto> douyinProductDtos) {
        TbSpu spu = null;
        List<TbSpu> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(douyinProductDtos)) {
            for (DouyinProductDto dto : douyinProductDtos) {
                spu = new TbSpu();
                spu.setCreateTime(new Date());
                spu.setBrandId(0L);
                spu.setTitle(dto.getName());
                spu.setCid1(dto.getFirst_cid().longValue());
                spu.setCid2(dto.getSecond_cid().longValue());
                spu.setCid3(dto.getThird_cid().longValue());
                spu.setSaleable(1);
                spu.setSpuDescrition(dto.getDescription());
                spu.setSubTitle(dto.getName());
                spu.setValid(1);
                list.add(spu);
            }
        }
        spuService.saveBatch(list);
    }



}
