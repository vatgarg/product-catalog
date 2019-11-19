package com.respect.learn.catalogservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.respect.learn.catalogservice.dao.IGenericDao;
import com.respect.learn.catalogservice.dbmodels.Category;
import com.respect.learn.catalogservice.dbmodels.Product;

//@Service
public class ProductService implements IService {

	IGenericDao<Product> dao;

	@Autowired
	public void setDao(IGenericDao<Product> daoToSet) {
		dao = daoToSet;
		dao.setClazz(Product.class);
	}

	public List<Product> findAll() {
		return dao.findAll();
	}

	public Product findById(int id) {
		return dao.findOne(id);
	}

	public void create(Product resource) {
		resource.getProductCategories();
	}

	public void update(Product resource) {
		dao.update(resource);

	}

	public void deleteById(int id) {
		dao.deleteById(id);

	}

}
