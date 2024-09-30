package com.osamo.dreamshops.service.product;

import com.osamo.dreamshops.exceptions.ProductNotFoundException;
import com.osamo.dreamshops.model.Category;
import com.osamo.dreamshops.model.Product;
import com.osamo.dreamshops.repository.CategoryRepository;
import com.osamo.dreamshops.repository.ProductRepository;
import com.osamo.dreamshops.request.AddProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product addProduct(AddProductRequest request) {
        // Check if the category is found in the DB
        //If Yes, set it as new product category
        //If No, save if a new category
        //Then set it as new product category

        Category category = Optional.ofNullable(categoryRepository
                .findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
        });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));

    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category

        );

    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
       productRepository.findById(id).ifPresentOrElse(productRepository::delete,
               () -> { throw new ProductNotFoundException("Product not found!");
       });
    }

    @Override
    public void updateProduct(Product product, Long productId) {

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
