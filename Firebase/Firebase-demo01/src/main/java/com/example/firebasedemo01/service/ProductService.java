package com.example.firebasedemo01.service;

import com.example.firebasedemo01.entity.Product;
import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ProductService {

    private static final String COLLECTION_NAME = "products";

    public String save(Product product) throws ExecutionException, InterruptedException {
        if (isExisted(product.getName())) {
            return "Product name is duplicated";
        }
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document();
        // Trick is here, get id of doc then ad to entity data
        product.setId(collectionApiFuture.getId());
        ApiFuture<WriteResult> apiFuture = collectionApiFuture.set(product);
        return apiFuture.get().getUpdateTime().toString();
    }

    public Product getDetailByName(String name) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(name);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        Product product = null;
        if (document.exists()) {
            product = document.toObject(Product.class);
            return product;
        } else {
            return null;
        }
    }

    public String update(Product product) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document().set(product);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String delete(String name) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(COLLECTION_NAME).document(name).delete();
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public boolean isExisted(String name) throws ExecutionException, InterruptedException {
        CollectionReference branches = FirestoreClient.getFirestore().collection(COLLECTION_NAME);
        Query query = branches.whereEqualTo("name", name);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        return !querySnapshot.get().isEmpty();
    }
}
