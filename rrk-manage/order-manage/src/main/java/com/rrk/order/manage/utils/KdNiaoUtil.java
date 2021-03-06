package com.rrk.order.manage.utils;

import com.rrk.common.handle.MyException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;



/**
 * 整合第三方物流（快递鸟）接口的工具类
 */
public class KdNiaoUtil {


    private static String URL = "http://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";

    private static String EBUSINESSID = "1397274";

    private static String APPKEY = "5900ac0e-299a-4dba-a7d8-010819442994";
//
//    //测试第三方物流数据
//    public static void main(String[] args) {
//        KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();
//        try {
//            String result = api.getOrderTracesByJson("ANE", "210001633605");
//            System.out.print(result);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 即时查询工具
     */

    /**
     * Json方式 查询订单物流轨迹
     * expCode： 快递公司的id
     * expNo：运单id
     * @throws Exception
     */
    public static String getOrderTracesByJson(String expCode, String expNo) throws MyException {
        String requestData = "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBUSINESSID);
        params.put("RequestType", "1002");
       // params.put("CustomerName","徐少彬");
        String dataSign = null;
        try {
            dataSign = encrypt(requestData,APPKEY, "UTF-8");
            params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
            params.put("DataType", "2");
            String result = sendPost(URL, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }


        //根据公司业务处理返回的信息......

        return null;
    }

    /**
     *
     * @param content
     * @param keyValue
     * @param charset
     * @return
     */
    private static  String encrypt(String content, String keyValue, String charset) throws Exception {
        if (keyValue != null)
        {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }

    /**
     * @param str
     * @param charset
     * @return
     */
    private static String urlEncoder(String str, String charset) throws MyException {

        String result = null;
        try {
            result = URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            java.net.URL realUrl = new URL(url);
            HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if(param.length()>0){
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    //System.out.println(entry.getKey()+":"+entry.getValue());
                }
                //System.out.println("param:"+param.toString());
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();

    }

    /**
     * 物流跟踪查询工具
     */
    private static String base64(String str, String charset) throws UnsupportedEncodingException{
        String encoded = base64Encode(str.getBytes(charset));
        return encoded;
    }

    private static char[] base64EncodeChars = new char[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/' };

    public static String base64Encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }


    private static String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

}

