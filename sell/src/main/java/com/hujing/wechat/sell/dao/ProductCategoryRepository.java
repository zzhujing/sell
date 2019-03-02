package com.hujing.wechat.sell.dao;

import com.hujing.wechat.sell.po.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hj
 * @time 2019-02-21 23:28
 * @description
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
