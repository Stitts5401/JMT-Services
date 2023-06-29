package com.stitts.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.stitts.productservice.dto.ProductRequest;
import com.stitts.productservice.models.Product;
import com.stitts.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository interestRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry ){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl );
	}

	@Test
	void shouldCreateInterestRequest() throws Exception {

		ProductRequest productRequest = getInterestRequest();
		String interestRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/interest")
				  .contentType(MediaType.APPLICATION_JSON)
				  .content(interestRequestString))
				.andExpect(status().isAccepted());
		Assertions.assertEquals(2, interestRepository.findAll().size());
	}

	@Test
	void shouldGetInterestRequest() throws Exception {

		ProductRequest productRequest = getInterestRequest();
		String interestRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/interest")
						.contentType(MediaType.APPLICATION_JSON)
						.content(interestRequestString))
				.andExpect(status().isAccepted());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/interest"))
				.andExpect(status().isOk())
				.andReturn();
		MockHttpServletResponse mvcResultResponse = mvcResult.getResponse();

		CollectionType listType =
				objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Product.class);
		List<Product> asList = objectMapper.readValue(
				mvcResultResponse.getContentAsString(), listType);
		Assertions.assertEquals( asList.get(0),  interestRepository.findAll().get(0));
	}

	private ProductRequest getInterestRequest() {
		return ProductRequest.builder()
				.name("John")
				.description("Interested in you.")
				.email("john@aol1q23.com")
				.phoneNumber(4121231234L)
				.build();
	}

}
