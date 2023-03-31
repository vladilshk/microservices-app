package ru.vovai.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;
import ru.vovai.inventoryservice.dto.InventoryResponse;
import ru.vovai.inventoryservice.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    //for test delete it please
    @Value("${eureka.instance.instance-id}")
    private String id;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/test")
    public String test(){
        return "hello " + id;
    }
}
