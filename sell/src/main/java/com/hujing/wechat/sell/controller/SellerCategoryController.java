package com.hujing.wechat.sell.controller;

import com.hujing.wechat.sell.form.CategoryForm;
import com.hujing.wechat.sell.po.ProductCategory;
import com.hujing.wechat.sell.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @time 2019-02-28 23:51
 * @description 卖家类目controller
 */
@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {


    @Autowired
    CategoryService categoryService;

    /**
     * 查看类目列表
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String getCategoryList(Model model) {
        List<ProductCategory> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "category/list";
    }

    /**
     * 跳转页面
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/index")
    public String index(@RequestParam(required = false) Integer categoryId, Model model) {
        if (categoryId != null) {
            //修改
            ProductCategory category = categoryService.findOne(categoryId);
            model.addAttribute("category", category);
        }
        return "category/index";
    }

    /**
     * 保存/新增
     * @param categoryForm
     * @param result
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid CategoryForm categoryForm, BindingResult result, Model model) {
        if (result.hasErrors()){
            model.addAttribute("msg", result.getFieldError().getDefaultMessage());
            model.addAttribute("returnUrl", "/sell/seller/category/list");
            return "common/error";
        }
        Integer categoryId = categoryForm.getCategoryId();
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(categoryForm, productCategory);
        categoryService.save(productCategory);
        return "redirect:/seller/category/list";
    }


}
