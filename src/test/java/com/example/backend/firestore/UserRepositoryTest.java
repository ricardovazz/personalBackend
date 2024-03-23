package com.example.backend.firestore;

import com.example.backend.firestore.User;
import com.example.backend.firestore.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Disabled
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByName() {
    // Create some test users
    User user1 = new User("John Doe");
    User user2 = new User("Jane Smith");
    User user3 = new User("John Smith");

    // Save the users to the repository
    Flux<User> saveUsers = userRepository.saveAll(Flux.just(user1, user2, user3))
        .thenMany(userRepository.findAll());

    // Verify that the users are saved successfully
    StepVerifier.create(saveUsers)
        .expectNext(user1, user2, user3)
        .verifyComplete();

    // Find users by name
    Mono<User> findUsers = userRepository.findByName("John Smith");

    // Verify that the correct users are found
    StepVerifier.create(findUsers)
        .expectNext(user3)
        .verifyComplete();
    }
}
