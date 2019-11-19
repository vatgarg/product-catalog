package com.respect.learn.catalogservice.dbmodels;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

//@Entity
public class Category implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer categoryId;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "category_product", joinColumns = { @JoinColumn(name = "category_id") }, inverseJoinColumns = {
			@JoinColumn(name = "product_id") })
	private List<Product> products = new ArrayList<Product>();

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(String name, List<Product> products) {
		this.name = name;
		this.products = products;
	}

	public Integer getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
	}

}
