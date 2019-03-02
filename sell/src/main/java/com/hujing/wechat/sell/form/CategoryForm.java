package com.hujing.wechat.sell.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author hj
 * @time 2019-03-01 0:02
 * @description 类目表单数据实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryForm {

    private Integer categoryId;

    /** 类目名字. */
    @NotNull(message = "类目名字不能为空")
    private String categoryName;

    /** 类目编号. */
    @NotNull(message = "类目编号不能为空")
    private Integer categoryType;
}
