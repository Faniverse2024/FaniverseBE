package fantastic.faniverse.product.ProductCategory.controller;

import fantastic.faniverse.product.ProductCategory.dto.ProductCategoryDto;
import fantastic.faniverse.product.ProductCategory.domain.ProductCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class ProductCategoryController {

    @GetMapping
    public ResponseEntity<List<ProductCategoryDto>> getAllProductCategories() {
        List<ProductCategoryDto> categories = ProductCategory.getAllCategories().stream()
                .map(category -> new ProductCategoryDto(
                        category.getTitle(),
                        category.getParentCategory() != null ? category.getParentCategory().getTitle() : null
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }
}
