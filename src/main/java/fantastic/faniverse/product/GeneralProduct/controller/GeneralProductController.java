package fantastic.faniverse.product.GeneralProduct.controller;

import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductRegisterRequest;
import fantastic.faniverse.product.GeneralProduct.service.GeneralProductServiceImpl;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class GeneralProductController {

    private final ProductServiceImpl productService;
    private final GeneralProductServiceImpl generalProductService;

    // 상품 수정
    @GetMapping("/update")
    public ResponseEntity<GeneralProductRegisterRequest> getUpdateProductPage(@RequestParam Long productId) {
        Product product = productService.findOne(productId);
        GeneralProduct generalProduct = (GeneralProduct) product;

        GeneralProductRegisterRequest form = GeneralProductRegisterRequest.builder()
                .title(generalProduct.getTitle())
                .category(generalProduct.getCategory())
                .content(generalProduct.getContent())
                .price(generalProduct.getPrice())
                .build();

        return ResponseEntity.ok(form);
    }

    // 상품 수정
    @PutMapping("/update")
    public ResponseEntity<Void> updateProduct(@RequestParam Long productId, @RequestParam Long userId, @RequestBody GeneralProductRegisterRequest form) throws IOException {
        generalProductService.updateProduct(form, userId, generalProductService.findOne(productId));
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/products/list/" + productId).build();
    }
}
