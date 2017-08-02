package com.huang.vo;

import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class GlobalResult implements Serializable {
    private static final long serialVersionUID = 3131808708432303266L;
    private String code;
    private String msg;
    private JSONObject data = new JSONObject();

    public GlobalResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 向业务数据集中添加数据
     *
     * @param key
     * @param value
     */
    public void putData(String key, Object value) {
        this.data.put(key, value);
    }

    /**
     * 向业务数据集中添加数据
     *
     * @param map
     */
    public void putData(Map<String, Object> map) {
        this.data.putAll(map);
    }

    /**
     * 从业数据集中获取数据
     *
     * @param key
     * @return
     */
    public Object obtainData(String key) {
        return this.data.get(key);
    }

    /**
     * 转换成json串
     */
    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", this.code);
        jsonObject.put("msg", this.msg);
        jsonObject.put("data", this.data.toString());
        return JSON.toJSONString(jsonObject);
    }

    public static void main(String[] args) {
        GlobalResult c = new GlobalResult();
        c.setCode("100");
        c.setMsg("test");
        c.putData("name", "1");
        c.putData("age", "2");
        String str = c.toJSONString();
        System.out.println(str);
        System.out.println(JSON.toJSONString(c));
    }
}
