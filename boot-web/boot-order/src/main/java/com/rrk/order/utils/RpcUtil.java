package com.rrk.order.utils;



import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: Administrator
 * @date: 2020-08-10 16:49
 */
public class RpcUtil {


    /**
     * 构建成功返回结果
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> createBizResult(Object obj) {
        Map<String, Object> resultMap = createResultMap(obj, RetEnum.RET_SUCCESS);
        return resultMap;
    }

    /**
     * 构建失败返回结果
     *
     * @param obj
     * @param retEnum
     * @return
     */
    public static Map<String, Object> createResult(Object obj, RetEnum retEnum) {
        if (retEnum == null) {
            retEnum = RetEnum.RET_PARAM_NOT_FOUND;
        }
        return createResultMap(obj, retEnum);
    }

    private static Map<String, Object> createResultMap(Object obj, RetEnum retEnum) {
        Map<String, Object> resultMap = new HashMap(3);
        resultMap.put("data", obj);
        resultMap.put("code", retEnum.getCode());
        resultMap.put("msg", retEnum.getMessage());
        return resultMap;
    }


    // 将request中的参数转换成Map
    public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;
            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }
        return retMap;
    }
}
