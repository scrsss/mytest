package com.qtc.controller;

import com.github.pagehelper.PageInfo;
import com.qtc.pojo.ProductInfo;
import com.qtc.pojo.vo.ProductInfoVo;
import com.qtc.service.ProductInfoService;
import com.qtc.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    public static final int PAGE_SIZE=5;
    String saveFileName="";
    @Autowired
    ProductInfoService productInfoService;
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list=productInfoService.getAll();
        request.setAttribute("list", list);
        return "product";
    }
    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo info=null;
        Object vo=request.getSession().getAttribute("prodVo");
        if (vo!=null){
            info=productInfoService.splitPageVo((ProductInfoVo)vo, PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        }else {
            info=productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.setAttribute("info", info);
        return "product";
    }
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(ProductInfoVo vo, HttpSession session){
        PageInfo info=productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);
    }
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session){
        List<ProductInfo> list=productInfoService.selectCondition(vo);
        session.setAttribute("list", list);
    }
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        saveFileName= FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());
        String path=request.getServletContext().getRealPath("/image_big");
        try {
            pimage.transferTo(new File(path+File.separator+saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject object=new JSONObject();
        object.put("imgurl", saveFileName);
        return object.toString();
    }
    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        int num=-1;
        try {
            num = productInfoService.save(info);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (num>0){
            request.setAttribute("msg", "增加成功");
        }else {
            request.setAttribute("msg", "增加失败");
        }
        saveFileName="";
        return "forward:/prod/split.action";
    }
    @RequestMapping("/one")
    public String one(int pid,ProductInfoVo vo,Model model,HttpSession session){
        ProductInfo info=productInfoService.getById(pid);
        model.addAttribute("prod",info);
        session.setAttribute("prodVo", vo);
        return "update";
    }
    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        if (!saveFileName.equals("")){
            info.setpImage(saveFileName);
        }
        int num=-1;
        try {
            num=productInfoService.update(info);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (num>0){
            request.setAttribute("msg", "更新成功");
        }else {
            request.setAttribute("msg", "更新失败");
        }
        saveFileName="";
        return "forward:/prod/split.action";
    }
    @RequestMapping("/delete")
    public String delete(int pid,ProductInfoVo vo,HttpServletRequest request){
        int num=-1;
        try {
            num = productInfoService.delete(pid);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (num>0){
            request.setAttribute("msg", "删除成功");
            request.getSession().setAttribute("deleteProdVo", vo);
        }else {
            request.setAttribute("msg", "删除失败");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }
    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request){
        PageInfo info=null;
        Object vo=request.getSession().getAttribute("deleteProdVo");
        if (vo!=null){
            info=productInfoService.splitPageVo((ProductInfoVo)vo , PAGE_SIZE);
        }else {
            info=productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info", info);
        return request.getAttribute("msg");
    }
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids,HttpServletRequest request){
        String []ps=pids.split(",");
        try {
            int num=productInfoService.deleteBatch(ps);
            if (num > 0) {
                request.setAttribute("msg", "批量删除成功");
            } else {
                request.setAttribute("msg", "批量删除失败");
            }
        }catch (Exception e){
            request.setAttribute("msg", "商品不可删除");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

}