package com.study.spring_study.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "list")
public class ProductList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    
    @Column
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "list_product",
        joinColumns = @JoinColumn(name = "id_list"),
        inverseJoinColumns = @JoinColumn(name = "id_product")
    )
    private List<Product> products = new ArrayList<>();
    
    public void addProduct(Product product){
        products.add(product);
    }
    public void removeProduct(Product product){
        products.removeIf(p -> p.getId().equals(product.getId()));
        // product.getLists().remove(this);
    }

    public ProductList(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }



}
