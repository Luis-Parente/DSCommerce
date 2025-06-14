package com.devsuperior.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DataBaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.tests.ProductFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	private Long existingId;

	private Long nonExistingId;

	private Long dependentId;

	private String productName;

	private Product product;

	private PageImpl<Product> page;

	private ProductDTO dto;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;

		productName = "PlayStation 5";

		product = ProductFactory.createProduct(productName);

		dto = new ProductDTO(product);

		page = new PageImpl<>(List.of(product));

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		Mockito.when(repository.searchByName(any(), (Pageable) any())).thenReturn(page);

		Mockito.when(repository.findAll((Pageable) any())).thenReturn(page);

		Mockito.when(repository.save(any())).thenReturn(product);

		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);

		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
		Assertions.assertEquals(result.getName(), product.getName());

	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});

	}

	@Test
	public void findAllShouldReturnPagedProductMinDTO() {
		Pageable pageable = PageRequest.of(0, 12);
		String name = "PlayStation 5";

		Page<ProductMinDTO> result = service.findAll(name, pageable);

		Assertions.assertNotNull(result);

		Assertions.assertEquals(result.getSize(), 1);

		Assertions.assertEquals(result.iterator().next().getName(), productName);

	}

	@Test
	public void insertShouldReturnProductDTO() {
		ProductDTO result = service.insert(dto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), product.getId());
		Assertions.assertEquals(result.getName(), product.getName());
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO result = service.update(existingId, dto);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), product.getId());
		Assertions.assertEquals(result.getName(), product.getName());
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, dto);
		});

	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});
	}

}
