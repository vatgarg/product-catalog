package com.respect.learn.catalogservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.respect.learn.catalogservice.dao.IGenericDao;
import com.respect.learn.catalogservice.dbmodels.Category;
import com.respect.learn.catalogservice.dbmodels.Product;

//@Service -- This has a problem with manytomany mapping done via jpa
public class CategoryService implements IService {

	IGenericDao<Category> dao;

	@Autowired
	public void setDao(IGenericDao<Category> daoToSet) {
		dao = daoToSet;
		dao.setClazz(Category.class);
	}

	public List<Category> findAll() {
		return dao.findAll();
	}

	public List<Product> findProdsForCategory(int categoryId) {
		Category category = dao.findOne(categoryId);
		return category.getProducts();
	}

	public Category findById(int id) {
		return dao.findOne(id);
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
