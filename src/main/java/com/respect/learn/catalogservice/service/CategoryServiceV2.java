package com.respect.learn.catalogservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.respect.learn.catalogservice.dbmodels.Category;
import com.respect.learn.catalogservice.dbmodels.Product;
import com.respect.learn.catalogservice.jdbcrepo.CategoryDbRepository;

@Service
public class CategoryServiceV2 {


	@Autowired
	CategoryDbRepository dao;

	public List<Category> findAll() {
		return dao.findAll();
	}

	public List<Product> findProdsForCategory(String name) {
		Category category = dao.findOne(name);
		return category.getProducts();
	}

	public Category findById(String name) {
		return dao.findOne(name);
	}
	
	public void create(Category resource) {
		dao.create(resource);
	}

	public void update(Category resource) {
		dao.update(resource);
	}

	public void deleteById(int id) {
		dao.deleteById(id);

	}

}
