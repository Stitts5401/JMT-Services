package com.stitts.orderingservice.service;

import com.stitts.orderingservice.dto.InventoryResponse;
import com.stitts.orderingservice.dto.OrderLineItemsDto;
import com.stitts.orderingservice.dto.OrderRequest;
import com.stitts.orderingservice.models.Order;
import com.stitts.orderingservice.models.OrderLineItems;
import com.stitts.orderingservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Call inventory service, and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam( "skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if(Arrays.stream(inventoryResponseArray).toList().isEmpty()){
            throw new IllegalArgumentException("Product is not in stock, please try again later.");
        }

        orderRepository.save(order);
        return "Order Placed Successfully";
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setYearsExperience(orderLineItemsDto.getYearsExperience());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
