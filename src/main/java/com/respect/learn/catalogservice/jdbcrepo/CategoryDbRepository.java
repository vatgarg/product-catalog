package com.respect.learn.catalogservice.jdbcrepo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.respect.learn.catalogservice.dbmodels.Category;
import com.respect.learn.catalogservice.dbmodels.Product;

@Repository
@Transactional
public class CategoryDbRepository {

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	public Category findOne(String name) {
		Category c = new Category("");
		SqlParameterSource namedParameters = new MapSqlParameterSource("name", name);
		return jdbcTemplate.query(Queries.FINDCATEOGY, namedParameters, new ResultSetExtractor<Category>() {

			@Override
			public Category extractData(ResultSet rs) throws SQLException, DataAccessException {
				boolean set = false;
				while (rs.next()) {
					if (!set) {
						c.setCategoryId(rs.getInt("CATEGORY_ID"));
						c.setName(rs.getString("NAME"));
						set = true;
					}
					Product p = new Product(0);
					p.setSerialNum(rs.getString("SERIAL_NUM"));
					p.setProductId(rs.getInt("PRODUCT_ID"));
					p.setDescription(rs.getString("DESCRIPTION"));
					p.setManufacturer(rs.getString("MANUFACTURER"));
					p.setProductName(rs.getString("PRODUCT_NAME"));

					if (c != null) {
						c.getProducts().add(p);
					}
				}
				return c;
			}
		});
	}

	public List<Category> findAll() {
		List<Category> toBeReturned = new ArrayList<Category>();
		return jdbcTemplate.query(Queries.FINDALLCATEGORY, new ResultSetExtractor<List<Category>>() {

			@Override
			public List<Category> extractData(ResultSet rs) throws SQLException, DataAccessException {
				int id = -1;
				boolean set = false;
				Category c = null;
				while (rs.next()) {
					if (!set || rs.getInt("CATEGORY_ID") != id) {
						c = new Category(rs.getString("NAME"));
						c.setCategoryId(rs.getInt("CATEGORY_ID"));
						set = true;
						id = rs.getInt("CATEGORY_ID");
						toBeReturned.add(c);
					}
					Product p = new Product(rs.getInt("PRODUCT_ID"));
					p.setSerialNum(rs.getString("SERIAL_NUM"));
					p.setProductId(rs.getInt("PRODUCT_ID"));
					p.setDescription(rs.getString("DESCRIPTION"));
					p.setManufacturer(rs.getString("MANUFACTURER"));
					p.setProductName(rs.getString("PRODUCT_NAME"));
					set = true;

					if (c != null) {
						c.getProducts().add(p);
					}
				}
				return toBeReturned;
			}
		});
	}

	public List<Map<String, Object>> create(Category entity) {
		KeyHolder holder = new GeneratedKeyHolder();
		List<Product> products = entity.getProducts();
		for (Product product : products) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("DESCRIPTION", product.getDescription());
			namedParameters.addValue("MANUFACTURER", product.getManufacturer());
			namedParameters.addValue("PRODUCT_NAME", product.getProductName());
			namedParameters.addValue("SERIAL_NUM", product.getSerialNum());
			jdbcTemplate.update(Queries.INSERTPRODUCT, namedParameters, holder);
		}

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder newHolder = new GeneratedKeyHolder();
		namedParameters.addValue("name", entity.getName());
		jdbcTemplate.update(Queries.INSERTCATEGORY, namedParameters, newHolder);

		Map<String, Object> categoryId = newHolder.getKeyList().get(holder.getKeyList().size() - 1);

		for (int i = 0; i < holder.getKeyList().size(); i++) {
			namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("CATEGORY_ID", categoryId.get("CATEGORY_ID"));
			namedParameters.addValue("PRODUCT_ID", holder.getKeyList().get(i).get("PRODUCT_ID"));
			jdbcTemplate.update(Queries.INSERTPRODUCTCATEGORY, namedParameters);
		}
		return holder.getKeyList();
	}

	public void update(Category entity) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("name", entity.getName());
		jdbcTemplate.update(Queries.UPDATECATEGORY, namedParameters);

		if (entity.getProducts().size() > 0) {
			KeyHolder holder = new GeneratedKeyHolder();
			List<Product> products = entity.getProducts();
			for (Product product : products) {
				namedParameters = new MapSqlParameterSource();
				namedParameters.addValue("DESCRIPTION", product.getDescription());
				namedParameters.addValue("MANUFACTURER", product.getManufacturer());
				namedParameters.addValue("PRODUCT_NAME", product.getProductName());
				namedParameters.addValue("SERIAL_NUM", product.getSerialNum());
				jdbcTemplate.update(Queries.INSERTPRODUCT, namedParameters, holder);
			}

			Category category = findOne(entity.getName());

			for (int i = 0; i < holder.getKeyList().size(); i++) {
				namedParameters = new MapSqlParameterSource();
				namedParameters.addValue("CATEGORY_ID", category.getCategoryId());
				namedParameters.addValue("PRODUCT_ID", holder.getKeyList().get(i).get("PRODUCT_ID"));
				jdbcTemplate.update(Queries.INSERTPRODUCTCATEGORY, namedParameters);
			}
		}
	}

	public void delete(Category entity) {
		String name = entity.getName();
		entity = findOne(name);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", entity.getCategoryId());
		jdbcTemplate.update(Queries.DELETEPRODUCTCATEGORY_1, namedParameters);
		jdbcTemplate.update(Queries.DELETECATEGORY, namedParameters);
	}

	public void deleteById(int entityId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", entityId);
		jdbcTemplate.update(Queries.DELETEPRODUCTCATEGORY_1, namedParameters);
		jdbcTemplate.update(Queries.DELETECATEGORY, namedParameters);
	}

}
