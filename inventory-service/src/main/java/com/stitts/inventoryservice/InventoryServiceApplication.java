package com.stitts.inventoryservice;

import com.stitts.inventoryservice.model.Inventory;
import com.stitts.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory1 = new Inventory();
			inventory1.setQuantity(12);
			inventory1.setSkuCode("selenium_grid");

			Inventory inventory2 = new Inventory();
			inventory2.setQuantity(1);
			inventory2.setSkuCode("postrgres_sql");

			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
		};
	}
}
