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
public class ProductDbRepository {

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	public Product findOne(String name) {
		Product p = new Product(0);
		SqlParameterSource namedParameters = new MapSqlParameterSource("name", name);
		return jdbcTemplate.query(Queries.FINDPRODUCT, namedParameters, new ResultSetExtractor<Product>() {

			@Override
			public Product extractData(ResultSet rs) throws SQLException, DataAccessException {
				boolean set = false;
				while (rs.next()) {
					if (!set) {
						p.setSerialNum(rs.getString("SERIAL_NUM"));
						p.setProductId(rs.getInt("PRODUCT_ID"));
						p.setDescription(rs.getString("DESCRIPTION"));
						p.setManufacturer(rs.getString("MANUFACTURER"));
						p.setProductName(rs.getString("PRODUCT_NAME"));
						set = true;
					}
					if (p != null) {
						p.getProductCategories().add(new Category(rs.getString("NAME")));
					}
				}
				return p;
			}
		});
	}

	public List<Product> findAll() {
		List<Product> toBeReturned = new ArrayList<Product>();
		return jdbcTemplate.query(Queries.FINDALLPRODUCT, new ResultSetExtractor<List<Product>>() {

			@Override
			public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
				int id = -1;
				boolean set = false;
				Product p = null;
				while (rs.next()) {
					if (!set || rs.getInt("PRODUCT_ID") != id) {
						p = new Product(rs.getInt("PRODUCT_ID"));
						p.setSerialNum(rs.getString("SERIAL_NUM"));
						p.setProductId(rs.getInt("PRODUCT_ID"));
						p.setDescription(rs.getString("DESCRIPTION"));
						p.setManufacturer(rs.getString("MANUFACTURER"));
						p.setProductName(rs.getString("PRODUCT_NAME"));
						id = rs.getInt("PRODUCT_ID");
						set = true;
						toBeReturned.add(p);
					}
					if (p != null) {
						p.getProductCategories().add(new Category(rs.getString("NAME")));
					}
				}
				return toBeReturned;
			}
		});
	}

	public List<Map<String, Object>> create(Product entity) {
		KeyHolder holder = new GeneratedKeyHolder();
		List<Category> categories = entity.getProductCategories();
		for (Category category : categories) {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("name", category.getName());
			jdbcTemplate.update(Queries.INSERTCATEGORY, namedParameters, holder);
		}

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder newHolder = new GeneratedKeyHolder();
		namedParameters.addValue("DESCRIPTION", entity.getDescription());
		namedParameters.addValue("MANUFACTURER", entity.getManufacturer());
		namedParameters.addValue("PRODUCT_NAME", entity.getProductName());
		namedParameters.addValue("SERIAL_NUM", entity.getSerialNum());
		jdbcTemplate.update(Queries.INSERTPRODUCT, namedParameters, newHolder);

		Map<String, Object> productId = newHolder.getKeyList().get(holder.getKeyList().size() - 1);
		

		for (int i = 0; i < holder.getKeyList().size(); i++) {
			namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("PRODUCT_ID", productId.get("PRODUCT_ID"));
			namedParameters.addValue("CATEGORY_ID", holder.getKeyList().get(i).get("CATEGORY_ID"));
			jdbcTemplate.update(Queries.INSERTPRODUCTCATEGORY, namedParameters);
		}
		return holder.getKeyList();
	}

	public void update(Product entity) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("DESCRIPTION", entity.getDescription());
		namedParameters.addValue("MANUFACTURER", entity.getManufacturer());
		namedParameters.addValue("PRODUCT_NAME", entity.getProductName());
		namedParameters.addValue("SERIAL_NUM", entity.getSerialNum());
		jdbcTemplate.update(Queries.UPDATEPRODUCT, namedParameters);
		
		if(entity.getProductCategories().size()>0) {
			KeyHolder holder = new GeneratedKeyHolder();
			List<Category> categories = entity.getProductCategories();
			for (Category category : categories) {
			    namedParameters = new MapSqlParameterSource();
				namedParameters.addValue("name", category.getName());
				jdbcTemplate.update(Queries.INSERTCATEGORY, namedParameters, holder);
			}
			
			Product product = findOne(entity.getProductName());
			
			for (int i = 0; i < holder.getKeyList().size(); i++) {
				namedParameters = new MapSqlParameterSource();
				namedParameters.addValue("PRODUCT_ID", product.getProductId());
				namedParameters.addValue("CATEGORY_ID", holder.getKeyList().get(i).get("CATEGORY_ID"));
				jdbcTemplate.update(Queries.INSERTPRODUCTCATEGORY, namedParameters);
			}
		}
	}

	public void delete(Product entity) {
		String name = entity.getProductName();
		entity = findOne(name);
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", entity.getProductId());
		jdbcTemplate.update(Queries.DELETEPRODUCTCATEGORY, namedParameters);
		jdbcTemplate.update(Queries.DELETEPRODUCT, namedParameters);
	}

	public void deleteById(int entityId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", entityId);
		jdbcTemplate.update(Queries.DELETEPRODUCTCATEGORY, namedParameters);
		jdbcTemplate.update(Queries.DELETEPRODUCT, namedParameters);
	}

}
