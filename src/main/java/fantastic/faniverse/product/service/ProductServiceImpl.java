package fantastic.faniverse.product.service;
import fantastic.faniverse.Exception.ProductNotFoundException;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.AuctionProduct.repository.AuctionProductRepository;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.repository.GeneralProductRepository;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.product.dto.ProductDto;
import fantastic.faniverse.product.repository.ProductRepository;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Getter
@Transactional
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final GeneralProductRepository generalProductRepository;
    private final AuctionProductRepository auctionProductRepository;

    //전체 상품 반환
    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    //전체상품 중 상품 1개 찾기
    @Override
    public Product findOne(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));
    }

    //상품 삭제
    @Override
    public void deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
        }
    }

    //홈화면 - 최근 등록된 상품
    @Override
    public List<ProductDetailsResponse> getRecentProducts() {
        return productRepository.findTop10ByOrderByCreatedAtDesc(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(product -> product.toProductDetail())
                .collect(Collectors.toList());
    }

    //카테고리별 상품
    @Override
    public List<ProductDetailsResponse> getProductsByCategory(List<String> categories) {
        // 각 카테고리(총 11개)에서 상품 1개씩 반환
        // 각 카테고리에서 첫 번째 상품만 가져옴
        return categories.stream()
                .flatMap(category -> productRepository.findByCategory(category)
                        .stream()
                        .findFirst()
                        .map(Product::toProductDetail)
                        .stream())
                .collect(Collectors.toList());
    }

    //가능한 GeneralProduct의 Status 출력
    public List<GeneralProductStatus> getChangeableStatusForGeneralProduct(GeneralProductStatus status) {
        return stream(GeneralProductStatus.values())
                .filter((item) -> item != status).collect(Collectors.toList());
    }


    //가능한 AuctionProduct의 Status 출력
    public List<AuctionProductStatus> getChangeableStatusForAuctionProduct(AuctionProductStatus status) {
        return stream(AuctionProductStatus.values())
                .filter((item) -> item != status).collect(Collectors.toList());
    }

    public Long addProduct(GeneralProduct generalProduct, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        generalProduct.setSeller(seller);
        generalProductRepository.save(generalProduct);
        return generalProduct.getId();
    }

    public Long addProduct(AuctionProduct auctionProduct, Long userId) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        auctionProduct.setSeller(seller);
        auctionProductRepository.save(auctionProduct);
        return auctionProduct.getId();
    }
}
