package com.hujing.wechat.sell.service.impl;

import com.hujing.wechat.sell.dao.ProductCategoryRepository;
import com.hujing.wechat.sell.po.ProductCategory;
import com.hujing.wechat.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hj
 * @time 2019-02-22 11:23
 * @description 商品类目业务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryRepository categoryRepository;

    /**
     * 根据id查询单条类目
     * @param categoryId id
     * @return
     */
    @Override
    public ProductCategory findOne(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    /**
     * 查询所有类目信息
     * @return
     */
    @Override
    public List<ProductCategory> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * 根据类目类型list查询对应的商品类目信息
     * @param categoryTypeList
     * @return
     */
    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return categoryRepository.findByCategoryTypeIn(categoryTypeList);
    }

    /**
     * 保存或修改商品类目
     * @param productCategory
     * @return
     */
    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return categoryRepository.save(productCategory);
    }

    /**
     * 根据id删除类目信息
     * @param categoryId
     */
    @Override
    public void delete(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
