package com.study.spring_study.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 240)
    private String description;

    @Column
    private String picture;

    @Column
    private boolean bought;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = false, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<StoreLink> links = new ArrayList<>();

    public void addLink(StoreLink link) {
        links.add(link);
        link.setProduct(this);
    }

    public void removeLink(StoreLink link) {
        links.remove(link);
        link.setProduct(null);
    }
}