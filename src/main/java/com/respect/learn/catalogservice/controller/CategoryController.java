package com.respect.learn.catalogservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.respect.learn.catalogservice.dbmodels.Category;
import com.respect.learn.catalogservice.service.CategoryService;
import com.respect.learn.catalogservice.service.CategoryServiceV2;

@RestController
@RequestMapping("/Category")
public class CategoryController {

	@Autowired
	private CategoryServiceV2 service;

	@GetMapping(produces = {"application/json",MediaType.APPLICATION_XML_VALUE})
	public List<Category> findAll() {
		return service.findAll();
	}

	@GetMapping(value = "/{name}", produces = {"application/json",MediaType.APPLICATION_XML_VALUE})
	public Category findById(@PathVariable("name") String name) {
		return RestPreconditions.checkFound(service.findById(name));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody Category resource) {
		Preconditions.checkNotNull(resource);
		service.create(resource);
	}

	@PutMapping(value = "/{name}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable("name") String name, @RequestBody Category resource) {
		RestPreconditions.checkFound(resource);
		Preconditions.checkNotNull(service.findById(name));
		service.update(resource);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") int id) {
		service.deleteById(id);
	}

	public static class RestPreconditions {
		public static <T> T checkFound(T resource) {
			if (resource != null) {
				return resource;
			}
			return resource;
		}
	}

}
