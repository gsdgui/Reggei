package com.example.reggei.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应数据最终都会封装成此对象
 * @author Hao zeng
 * @create 2022-06-22 23:29
 */

@Data
public class R<T> {

    private Integer code; // 编码：1成功，0个其他数字失败

    private String msg; // 错误信息

    private T data; // 数据

    private Map map = new HashMap(); // 动态数据

    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg){
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value){
        this.map.put(key, value);
        return this;
    }

}
