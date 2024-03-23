package com.example.backend.firestore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebFluxTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(new UserController(userRepository)).build();
    }

    @Test
    public void testGetAllUsers() {
        // Mock the UserRepository to return a Flux of users
        when(userRepository.findAll()).thenReturn(Flux.just(new User("John"), new User("Jane")));

        // Send a GET request to "/users" and verify the response
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(2)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains(new User("John"), new User("Jane"));
                });

        // Verify that the UserRepository's getAllUsers method was called once
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUser() {
        // Mock the UserRepository to return a Mono of a user
        when(userRepository.findByName("John")).thenReturn(Mono.just(new User("John")));

        // Send a GET request to "/users/John" and verify the response
        webTestClient.get().uri("/users/John")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).isEqualTo(new User("John"));
                });


        // Verify that the UserRepository's getUser method was called once with the argument "John"
        verify(userRepository, times(1)).findByName("John");
    } 
    
    @Test
    public void testSaveUser() {
        // Mock the UserRepository to return a Mono of a user
        when(userRepository.save(new User("John"))).thenReturn(Mono.just(new User("John")));

        // Send a POST request to "/users" with the body {"name": "John"} and verify the response
        webTestClient.post().uri("/users")
                .bodyValue(new User("John"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(new User("John"));

        // Verify that the UserRepository's saveUser method was called once with the argument "John"
        verify(userRepository, times(1)).save(new User("John"));
    }

    @Test
    public void testDeleteUser() {
        // Mock the UserRepository to return a Mono of a user
        when(userRepository.deleteUserByName("John")).thenReturn(Mono.empty());

        // Send a DELETE request to "/users/John" and verify the response
        webTestClient.delete().uri("/users/John")
                .exchange()
                .expectStatus().isOk();

        // Verify that the UserRepository's deleteUser method was called once with the argument "John"
        verify(userRepository, times(1)).deleteUserByName("John");
    }

    @Test
    public void testGetUserNotFound() {
        // Mock the UserRepository to return an empty Mono
        when(userRepository.findByName("John")).thenReturn(Mono.empty());

        // Send a GET request to "/users/John" and verify the response
        webTestClient.get().uri("/users/John")
                .exchange()
                .expectStatus().isNotFound();

        // Verify that the UserRepository's getUser method was called once with the argument "John"
        verify(userRepository, times(1)).findByName("John");
    }

    @Test
    public void testGetAllUsersEmpty() {
        // Mock the UserRepository to return an empty Flux
        when(userRepository.findAll()).thenReturn(Flux.empty());

        // Send a GET request to "/users" and verify the response
        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                        .hasSize(0);

        // Verify that the UserRepository's getAllUsers method was called once
        verify(userRepository, times(1)).findAll();
    }
}
