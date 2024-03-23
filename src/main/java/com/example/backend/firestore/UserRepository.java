package com.example.backend.firestore;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UserRepository extends FirestoreReactiveRepository<User> {
    Mono<User> findByName(String name);

    Flux<User> findAll();

    Mono<Void> deleteUserByName(String name);

    Mono<User> save(User user);

}
