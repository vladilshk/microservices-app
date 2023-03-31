package ru.vovai.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vovai.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String > {

}
