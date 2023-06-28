package com.stitts.productservice.repository;

import com.stitts.productservice.models.Interest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterestRepository extends MongoRepository<Interest, String> {
}
