package com.qtc.service.impl;

import com.qtc.mapper.ProductTypeMapper;
import com.qtc.pojo.ProductType;
import com.qtc.pojo.ProductTypeExample;
import com.qtc.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    ProductTypeMapper productTypeMapper;
    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}