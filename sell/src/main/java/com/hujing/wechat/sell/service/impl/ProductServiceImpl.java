package com.hujing.wechat.sell.service.impl;

import com.hujing.wechat.sell.dao.ProductCategoryRepository;
import com.hujing.wechat.sell.dao.ProductInfoRepository;
import com.hujing.wechat.sell.dto.CartDTO;
import com.hujing.wechat.sell.enums.ProdStateEnum;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import com.hujing.wechat.sell.po.ProductCategory;
import com.hujing.wechat.sell.po.ProductInfo;
import com.hujing.wechat.sell.service.ProductService;
import com.hujing.wechat.sell.vo.ProdDetailVo;
import com.hujing.wechat.sell.vo.ProdInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hj
 * @time 2019-02-23 14:45
 * @description 商品信息业务实现类
 */
@Service
@CacheConfig(cacheNames = "product")
public class ProductServiceImpl implements ProductService {


    @Autowired
    ProductInfoRepository infoRepository;

    @Autowired
    ProductCategoryRepository categoryRepository;

    /**
     * 按id查询一件商品
     *
     * @param id
     * @return
     */
    @Override
    @Cacheable(key="#id",unless = "#id == null || #id == ''")
    public ProductInfo findOne(String id) {
        return infoRepository.findById(id).orElse(null);
    }

    /**
     * 查询所有已经上架的商品
     *
     * @return
     */
    @Override
    public List<ProductInfo> findUpAll() {
        return infoRepository.findByProductStatus(ProdStateEnum.UP.getCode());
    }

    /**
     * 分页查询所有商品
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return infoRepository.findAll(pageable);
    }

    /**
     * 添加商品
     *
     * @param info
     * @return
     */
    @Override
    @CachePut(key = "#info.productId",unless = "#info ==null || #info.productId == null  ")
    public ProductInfo save(ProductInfo info) {
        return infoRepository.save(info);
    }

    /**
     * 查询所有的商品和类目信息并且封装到ResultVo中
     *
     * @return
     */
    @Override
    public List<ProdDetailVo> queryProdList() {
        List<ProdDetailVo> result = new LinkedList<>();

        //1.查询所有的商品类型信息
        List<ProductCategory> categoryList = categoryRepository.findAll();
        //2.使用类型的type查询所有的商品
        categoryList.forEach(category -> {

            Integer categoryType = category.getCategoryType();
            List<ProductInfo> prodInfoList = infoRepository.findByCategoryType(categoryType);
            List<ProdInfoVo> infoVoList = new LinkedList<>();

            prodInfoList.forEach(info -> {
                ProdInfoVo infoVo = new ProdInfoVo();
                BeanUtils.copyProperties(info, infoVo);
                infoVoList.add(infoVo);
            });

            ProdDetailVo prodDetailVo = ProdDetailVo
                    .builder()
                    .categoryName(category.getCategoryName())
                    .categoryType(categoryType)
                    .foods(infoVoList)
                    .build();
            result.add(prodDetailVo);
        });
        //3.填充ResultVo
        return result;
    }

    /**
     * 减库存
     *
     * @param cartDTOList
     */
    @Override
    public void decrStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo info = infoRepository.findById(cartDTO.getProductId()).
                    orElseThrow(() -> new SellException(ResultEnum.PRODUCT_NOT_EXIST));
            Integer currentStock = info.getProductStock();
            Integer productQuantity = cartDTO.getProductQuantity();
            if (currentStock > productQuantity) {
                info.setProductStock(currentStock - productQuantity);
                infoRepository.save(info);
            } else {
                throw new SellException(ResultEnum.STOCK_NOT_ENOUGH);
            }
        }
    }

    /**
     * 增加库存
     * @param cartDTOList
     */
    @Override
    public void incrStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo info = infoRepository.findById(cartDTO.getProductId()).
                    orElseThrow(() -> new SellException(ResultEnum.PRODUCT_NOT_EXIST));
            Integer currentStock = info.getProductStock();
            Integer productQuantity = cartDTO.getProductQuantity();
            info.setProductStock(currentStock + productQuantity);
            infoRepository.save(info);
        }
    }

    /**
     * 下架
     * @param productId
     */
    @Override
    public void offSale(String productId) {
        changeStatus(productId,ProdStateEnum.DOWN);
    }
    /**
     * 上架
     * @param productId
     */
    @Override
    public void onSale(String productId) {
        changeStatus(productId,ProdStateEnum.UP);
    }

    public void changeStatus(String productId,ProdStateEnum prodStateEnum) {
        ProductInfo info = infoRepository.findById(productId).orElseThrow(() -> new SellException(ResultEnum.PRODUCT_NOT_EXIST));
        Integer code = prodStateEnum.getCode();
        if (info.getProductStatus().equals(code)){
            throw new SellException(ResultEnum.PROD_STATUS_ERROR);
        }
        info.setProductStatus(code);
        infoRepository.save(info);
    }
}
