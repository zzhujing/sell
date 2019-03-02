package com.hujing.wechat.sell.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hj
 * @time 2019-02-23 15:08
 * @description 公共vo结果对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ResultVo<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 携带的数据信息
     */
    private T data;


    public static <T> ResultVo<T> success(T data) {
        return ResultVo
                .<T>builder()
                .msg("成功")
                .code(0)
                .data(data)
                .build();
    }
    public static <T> ResultVo<T> success(String msg,T data) {
        return ResultVo
                .<T>builder()
                .msg(msg)
                .code(0)
                .data(data)
                .build();
    }
    public static <T> ResultVo<T> success(Integer code,String msg,T data) {
        return ResultVo
                .<T>builder()
                .msg(msg)
                .code(code)
                .data(data)
                .build();
    }

    public static <T> ResultVo<T> error(Integer code, String errorMsg, T data) {
        return ResultVo
                .<T>builder()
                .msg(errorMsg)
                .code(code)
                .data(data)
                .build();
    }

    public static <T> ResultVo<T> error(Integer code, String errorMsg) {
        return ResultVo
                .<T>builder()
                .msg(errorMsg)
                .code(code)
                .data(null)
                .build();
    }

    public static <T> ResultVo<T> error(Integer code, T data) {
        return ResultVo
                .<T>builder()
                .msg("失败")
                .code(code)
                .data(null)
                .build();
    }

    public static <T> ResultVo<T> error() {
        return ResultVo
                .<T>builder()
                .msg("失败")
                .code(404)
                .data(null)
                .build();
    }



}
