package com.example.backend.firestore;

import com.google.cloud.firestore.annotation.DocumentId;

import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collectionName = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @DocumentId
	String name;
}

    