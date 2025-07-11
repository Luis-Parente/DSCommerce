package com.devsuperior.dscommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscommerce.dto.CategoryDTO;
import com.devsuperior.dscommerce.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/categories")
@Tag(name = "Categories", description = "Controller for Categories")
public class CategoryController {

	@Autowired
	private CategoryService service;

	@Operation(description = "Get all catgories", summary = "List all categories", responses = {
			@ApiResponse(description = "Ok", responseCode = "200"), })
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> dto = service.findAll();
		return ResponseEntity.ok(dto);
	}

}
