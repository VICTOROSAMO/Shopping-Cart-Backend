package com.osamo.dreamshops.controller;

import com.osamo.dreamshops.dto.ProductDto;
import com.osamo.dreamshops.exceptions.ResourceNotFoundException;
import com.osamo.dreamshops.model.Product;
import com.osamo.dreamshops.request.AddProductRequest;
import com.osamo.dreamshops.request.ProductUpdateRequest;
import com.osamo.dreamshops.response.ApiResponse;
import com.osamo.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")

public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        System.out.println(products);

        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        System.out.println(convertedProducts);

        return ResponseEntity.ok(new ApiResponse("Success!", convertedProducts));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Success!", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest){
        try {
            Product theProduct = productService.addProduct(productRequest);
            ProductDto productDto = productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Add Product Success", productDto));
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request,
                                                     @PathVariable Long productId){
        try {
            Product theProduct = productService.updateProduct(request,productId);
            return ResponseEntity.ok(new ApiResponse("Updated successfully!", theProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Deleted Successfully", null));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brandName,
                                                                 @RequestParam String productName){
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success", convertedProducts ));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category,
                                                                     @RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("products/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name){
        try {
            List<Product> products = productService.getProductsByName(name);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brandName){
        try {
            List<Product> products = productService.getProductsByBrand(brandName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("products/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category){
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND)
                        .body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("products/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,
                                                                   @RequestParam String name){
        try {
            var productsCount = productService.getProductsByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("Success", productsCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }


}
