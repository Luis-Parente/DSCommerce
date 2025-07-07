package com.devsuperior.dscommerce.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

	@Autowired
	private OrderService service;

	@Operation(description = "Get order by id", summary = "Get order by id", responses = {
			@ApiResponse(description = "Ok", responseCode = "200"),
			@ApiResponse(description = "Not Found", responseCode = "404"), })
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
		OrderDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@Operation(description = "Create a new order", summary = "Create a new order", responses = {
			@ApiResponse(description = "Created", responseCode = "201"),
			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "Unauthorized", responseCode = "401"),
			@ApiResponse(description = "Forbidden", responseCode = "403"),
			@ApiResponse(description = "Unprocessable Entity", responseCode = "422") })
	@PreAuthorize("hasAnyRole('ROLE_CLIENT')")
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping(produces = "application/json")
	public ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

}
