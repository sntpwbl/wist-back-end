package com.study.spring_study.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="person")
public class Product implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false, length=240)
    private String description;
    @Column
    private String picture;
    @Column(name="first_store")
    private String firstStore;
    @Column(name="second_store")
    private String secondStore;
    @Column(name="third_store")
    private String thirdStore;
    @Column(name="first_link")
    private String firstLink;
    @Column(name="second_link")
    private String secondLink;
    @Column(name="third_link")
    private String thirdLink;
}
