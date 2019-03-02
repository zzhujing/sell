package com.hujing.wechat.sell.service;

import com.hujing.wechat.sell.po.ProductCategory;

import java.util.List;

/**
 * @author hj
 * @time 2019-02-22 11:17
 * @description 商品类目业务层接口
 */
public interface CategoryService {

    /**
     * 根据id查询一个类目
     * @param categoryId id
     * @return 商品类目
     */
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    /**
     * 根据categoryTypeList查询类目列表
     * @param categoryTypeList
     * @return
     */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);


    ProductCategory save(ProductCategory productCategory);

    void delete(Integer categoryId);
}
