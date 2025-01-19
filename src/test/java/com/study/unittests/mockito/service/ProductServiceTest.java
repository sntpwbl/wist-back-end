package com.study.unittests.mockito.service;
// import static org.junit.jupiter.api.Assertions.*;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.hateoas.EntityModel;

// import static org.mockito.Mockito.when;
// import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
// import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// import com.study.spring_study.controller.ProductController;
// import com.study.spring_study.dto.ProductDTO;
// import com.study.spring_study.model.Product;
// import com.study.spring_study.model.StoreLink;
import com.study.spring_study.repository.ProductRepository;
import com.study.spring_study.service.ProductService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;
    
    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testFindById(){
    //     List<StoreLink> links = new ArrayList<>();
    //     Product product = new Product(1L, "test", "descTest", "picTest", false, links);
    //     when(repository.findById(1L)).thenReturn(Optional.of(product));

    //     EntityModel<ProductDTO> model = service.findById(1L);
    //     ProductDTO result = model.getContent();

    //     assertNotNull(result);
    //     assertEquals(1L, result.id());
    //     assertEquals("test", result.name());
    //     assertEquals("descTest", result.description());
    //     assertEquals("picTest", result.picture());

    //     assertTrue(model.hasLink("self"));
    //     assertTrue(model.hasLink("update"));
    //     assertTrue(model.hasLink("delete"));

    //     String selfHref = linkTo(methodOn(ProductController.class).findById(1L)).toUri().toString();
    //     assertEquals(selfHref, model.getLink("self").get().getHref());
    // }

}
