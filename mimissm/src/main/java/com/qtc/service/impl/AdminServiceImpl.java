package com.qtc.service.impl;

import com.qtc.mapper.AdminMapper;
import com.qtc.pojo.Admin;
import com.qtc.pojo.AdminExample;
import com.qtc.service.AdminService;
import com.qtc.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;
    @Override
    public Admin login(String name, String pwd) {
        AdminExample example=new AdminExample();
        example.createCriteria().andANameEqualTo(name);
        List<Admin> list=adminMapper.selectByExample(example);
        if (list.size()>0){
            Admin admin=list.get(0);
            String miPwd= MD5Util.getMD5(pwd);
            if (miPwd.equals(admin.getaPass())){
                return admin;
            }
        }
        return null;
    }
}