package com.hujing.wechat.sell.controller;

import com.hujing.wechat.sell.service.ProductService;
import com.hujing.wechat.sell.vo.ProdDetailVo;
import com.hujing.wechat.sell.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hj
 * @time 2019-02-23 15:13
 * @description 商品买家端控制层
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProdController {

    @Autowired
    ProductService productService;
    /**
     * 查询商品列表
     * @return
     */
    @GetMapping("/list")
    public ResultVo<List<ProdDetailVo>> queryProdList() {
        //调用service方法查询所有的商品信息
        return ResultVo.success(productService.queryProdList());
    }

}
