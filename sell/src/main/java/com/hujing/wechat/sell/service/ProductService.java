package com.hujing.wechat.sell.service;

import com.hujing.wechat.sell.dto.CartDTO;
import com.hujing.wechat.sell.po.ProductInfo;
import com.hujing.wechat.sell.vo.ProdDetailVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author hj
 * @time 2019-02-23 14:42
 * @description 商品业务层接口
 */
public interface ProductService {

    ProductInfo findOne(String id);

    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo info);

    List<ProdDetailVo> queryProdList();

    void decrStock(List<CartDTO> cartDTOList);

    void incrStock(List<CartDTO> cartDTOList);

    void offSale(String productId);
    void onSale(String productId);
}
