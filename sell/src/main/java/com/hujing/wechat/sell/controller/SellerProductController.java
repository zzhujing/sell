package com.hujing.wechat.sell.controller;

import com.hujing.wechat.sell.form.ProductForm;
import com.hujing.wechat.sell.po.ProductCategory;
import com.hujing.wechat.sell.po.ProductInfo;
import com.hujing.wechat.sell.service.CategoryService;
import com.hujing.wechat.sell.service.ProductService;
import com.hujing.wechat.sell.util.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @author hj
 * @time 2019-02-28 20:36
 * @description 买家商品管理控制层
 */
@Controller
@RequestMapping("/seller/product")
@Slf4j
public class SellerProductController {


    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;


    /**
     * 获取商品列表
     *
     * @param page
     * @param size
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String getProdList(@RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = "10") Integer size,
                              Model model) {
        Page<ProductInfo> prodPage = productService.findAll(PageRequest.of(page - 1, size));
        model.addAttribute("prodPage", prodPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        return "prod/list";
    }

    /**
     * 下架
     *
     * @param productId
     * @return
     */
    @GetMapping("/offSale")
    public String offSale(@RequestParam String productId, Model model) {
        try {
            productService.offSale(productId);
            model.addAttribute("returnUrl", "/sell/seller/product/list");
            return "common/success";
        } catch (Exception e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("returnUrl", "/sell/seller/product/list");
            return "common/error";
        }
    }
    /**
     * 上架
     *
     * @param productId
     * @return
     */
    @GetMapping("/onSale")
    public String onSale(@RequestParam String productId, Model model) {
        try {
            productService.onSale(productId);
            model.addAttribute("returnUrl", "/sell/seller/product/list");
            return "common/success";
        } catch (Exception e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("returnUrl", "/sell/seller/product/list");
            return "common/error";
        }
    }


    /**
     * 展示修改页面
     *
     * @param productId
     * @return
     */
    @GetMapping("/index")
    public String showUpdatePage(@RequestParam(required = false) String productId, Model model) {
        //1.查询商品
        if (!StringUtils.isEmpty(productId)) {
            ProductInfo prod = productService.findOne(productId);
            model.addAttribute("productInfo", prod);
        }
        //2.查询所有的类目
        List<ProductCategory> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "prod/index";
    }

    /**
     * 修改/新增product
     *
     * @param productForm
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid ProductForm productForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("msg", bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("returnUrl", "/sell/seller/product/index");
            return "common/error";
        }
        try {
            ProductInfo productInfo = new ProductInfo();
            String productId = productForm.getProductId();
            if (StringUtils.isEmpty(productId)) {
                //说明是新增
                productForm.setProductId(KeyGenerator.generate());
            } else {
                //修改
                productInfo = productService.findOne(productId);
            }
            BeanUtils.copyProperties(productForm, productInfo);
            productService.save(productInfo);
        } catch (BeansException e) {
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("returnUrl", "/sell/seller/product/index");
            return "common/error";
        }

        model.addAttribute("returnUrl", "/sell/seller/product/list");
        return "common/success";
    }


}
