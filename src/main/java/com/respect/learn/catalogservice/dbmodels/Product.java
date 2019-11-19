package com.respect.learn.catalogservice.dbmodels;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

//@Entity
public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToMany(mappedBy = "products", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JsonIgnore
	private List<Category> categorys = new ArrayList<Category>();

	@Column(unique = true, nullable = false)
	private String productName;
	private String description;
	private String serialNum;
	private String manufacturer;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int productId;

	public Product() {
	}

	public Product(int id) {
		this.productId = id;
	}

	public Product(int id, List<Category> productCategories) {
		this.productId = id;
		this.categorys = productCategories;
	}

	public List<Category> getProductCategories() {
		return this.categorys;
	}

	public void setProductCategories(List<Category> productCategories) {
		this.categorys = productCategories;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSerialNum() {
		return this.serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Product))
			return false;
		Product castOther = (Product) other;

		return ((this.getProductName() == castOther.getProductName()) || (this.getProductName() != null
				&& castOther.getProductName() != null && this.getProductName().equals(castOther.getProductName())))
				&& ((this.getDescription() == castOther.getDescription())
						|| (this.getDescription() != null && castOther.getDescription() != null
								&& this.getDescription().equals(castOther.getDescription())))
				&& ((this.getSerialNum() == castOther.getSerialNum()) || (this.getSerialNum() != null
						&& castOther.getSerialNum() != null && this.getSerialNum().equals(castOther.getSerialNum())))
				&& ((this.getManufacturer() == castOther.getManufacturer())
						|| (this.getManufacturer() != null && castOther.getManufacturer() != null
								&& this.getManufacturer().equals(castOther.getManufacturer())))
				&& (this.getProductId() == castOther.getProductId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getProductName() == null ? 0 : this.getProductName().hashCode());
		result = 37 * result + (getDescription() == null ? 0 : this.getDescription().hashCode());
		result = 37 * result + (getSerialNum() == null ? 0 : this.getSerialNum().hashCode());
		result = 37 * result + (getManufacturer() == null ? 0 : this.getManufacturer().hashCode());
		result = 37 * result + this.getProductId();
		return result;
	}

}
