package ru.vovai.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import ru.vovai.orderservice.config.WebClientConfig;
import ru.vovai.orderservice.dto.InventoryResponse;
import ru.vovai.orderservice.dto.OrderLineItemsDto;
import ru.vovai.orderservice.dto.OrderRequest;
import ru.vovai.orderservice.model.Order;
import ru.vovai.orderservice.model.OrderLineItems;
import ru.vovai.orderservice.repositoty.OrderRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        var orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDTO).toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String > skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();


        //Call inventory Service, and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://localhost:8083/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build() )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw  new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItems mapToDTO(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
