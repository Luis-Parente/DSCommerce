package com.devsuperior.dscommerce.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.entities.enums.OrderStatus;
import com.devsuperior.dscommerce.tests.ProductFactory;
import com.devsuperior.dscommerce.tests.TokenUtil;
import com.devsuperior.dscommerce.tests.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TokenUtil tokenUtil;

	private String adminUsername, adminPassword, adminToken;

	private String clientUsername, clientPassword, clientToken;

	private String invalidToken;

	private Long existingOrderId, nonExistingOrderId;

	private Order order;
	private OrderDTO orderDTO;
	private User user;

	@BeforeEach
	void setUp() throws Exception {
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";

		clientUsername = "maria@gmail.com";
		clientPassword = "123456";

		existingOrderId = 1L;

		nonExistingOrderId = 100L;

		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		invalidToken = adminToken + "xpto";

		user = UserFactory.createClientUser();

		order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user);

		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);

	}

	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistisAndAdminLogged() throws Exception {
		ResultActions result = mockMvc.perform(get("/orders/{id}", existingOrderId)
				.header("Authorization", "Bearer " + adminToken).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingOrderId));
		result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("PAID"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());

	}

	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistisAndClientLoggedAndOrderBelongUser() throws Exception {
		ResultActions result = mockMvc.perform(get("/orders/{id}", existingOrderId)
				.header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingOrderId));
		result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("PAID"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());

	}

	@Test
	public void findByIdShouldReturnForbiddenWhenIdExistisAndClientLoggedAndOrderDoesNotBelongUser() throws Exception {
		Long otherOrderId = 2L;

		ResultActions result = mockMvc.perform(get("/orders/{id}", otherOrderId)
				.header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());

	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistisAndAdminLogged() throws Exception {

		ResultActions result = mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
				.header("Authorization", "Bearer " + adminToken).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());

	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExistisAndClientLogged() throws Exception {

		ResultActions result = mockMvc.perform(get("/orders/{id}", nonExistingOrderId)
				.header("Authorization", "Bearer " + clientToken).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());

	}

	@Test
	public void findByIdShouldReturnUnauthorizedWhenNotLogged() throws Exception {

		ResultActions result = mockMvc.perform(get("/orders/{id}", existingOrderId)
				.header("Authorization", "Bearer " + invalidToken).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());

	}
}
