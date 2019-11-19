package com.respect.learn.catalogservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.respect.learn.catalogservice.dbmodels.Product;
import com.respect.learn.catalogservice.jdbcrepo.ProductDbRepository;

@Service
public class ProductServiceV2 implements IService {

	@Autowired
	ProductDbRepository dao;

	public List<Product> findAll() {
		return dao.findAll();
	}

	public Product findById(String name) {
		return dao.findOne(name);
	}

	public void create(Product resource) {
		dao.create(resource);
	}

	public void update(Product resource) {
		dao.update(resource);

	}

	public void deleteById(int id) {
		dao.deleteById(id);

	}

}
