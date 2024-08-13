package fantastic.faniverse.product.controller;

import fantastic.faniverse.chat.application.ChatService;
import fantastic.faniverse.chat.application.ChatRoomService;
import fantastic.faniverse.chat.domain.ChatRoom;
import fantastic.faniverse.product.ProductCategory.domain.ProductCategory;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.AuctionProduct.service.AuctionProductServiceImpl;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductRegisterRequest;
import fantastic.faniverse.product.AuctionProduct.dto.AuctionProductRegisterRequest;
import fantastic.faniverse.product.GeneralProduct.service.GeneralProductServiceImpl;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.product.service.ProductServiceImpl;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final GeneralProductServiceImpl generalProductService;
    private final AuctionProductServiceImpl auctionProductService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final List<String> categoryList = getCategoryList();

    // 전체 상품 리스트 출력
    @GetMapping("/list")
    public ResponseEntity<List<ProductDetailsResponse>> getProducts(@RequestParam Long userId) {
        List<ProductDetailsResponse> products = productService.findAllProducts().stream()
                .map(ProductDetailsResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    // 일반 상품 등록 페이지 데이터 제공
    @GetMapping("/generalproducts/register")
    public ResponseEntity<GeneralProductRegisterRequest> getGeneralProductRegisterPage() {
        GeneralProductRegisterRequest form = new GeneralProductRegisterRequest();
        return ResponseEntity.ok(form);
    }

    // 경매 상품 등록 페이지 데이터 제공
    @GetMapping("/auctionproducts/register")
    public ResponseEntity<AuctionProductRegisterRequest> getAuctionProductRegisterPage() {
        AuctionProductRegisterRequest form = new AuctionProductRegisterRequest();
        return ResponseEntity.ok(form);
    }

    // 일반 상품 등록
    @PostMapping("/generalproducts/register")
    public ResponseEntity<Void> registerGeneralProduct(@RequestParam Long userId, @RequestBody GeneralProductRegisterRequest form) throws IOException {
        Long productId = generalProductService.saveGeneralProduct(form, userId);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/products/" + productId).build();
    }

    // 경매 상품 등록
    @PostMapping("/auctionproducts/register")
    public ResponseEntity<Void> registerAuctionProduct(@RequestParam Long userId, @RequestBody AuctionProductRegisterRequest form) throws IOException {
        Long productId = auctionProductService.saveAuctionProduct(form, userId);
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", "/products/" + productId).build();
    }

    // 상품 상세 정보 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsResponse> getProductDetail(@RequestParam Long userId, @PathVariable Long productId, @PathVariable Long roomId) {
        User user = userService.findUserById(userId);
        Product product = productService.findOne(productId);
        // roomId를 사용하여 채팅방 조회
        ChatRoom chatRoom = chatRoomService.findChatRoomById(roomId, user, productId);

        // ProductDetailsResponse 객체로 변환
        ProductDetailsResponse productDetail = product.toProductDetail();
        return ResponseEntity.ok(productDetail);
    }

    // 판매 상품 상태 변경
    @PutMapping("/{productId}/status")
    public ResponseEntity<Void> updateProductStatus(@PathVariable Long productId, @RequestParam String status) {
        Product product = productService.findOne(productId);
        if (product instanceof GeneralProduct) {
            GeneralProductStatus generalStatus = GeneralProductStatus.valueOf(status);
            generalProductService.updateGeneralProductStatus(productId, generalStatus);
        } else {
            AuctionProductStatus auctionStatus = AuctionProductStatus.valueOf(status);
            auctionProductService.updateAuctionProductStatus(productId, auctionStatus);
        }
        return ResponseEntity.ok().build();
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // 홈화면 - 최근 등록된 상품 조회, 카테고리별 상품 조회
    @GetMapping("/home")
    public ResponseEntity<Map<String, List<ProductDetailsResponse>>> getHomePage(@RequestParam List<String> categories) {
        List<ProductDetailsResponse> recentProducts = productService.getRecentProducts(); // 최근 상품 10개
        List<ProductDetailsResponse> firstItemInCategory = productService.getProductsByCategory(categories); // 카테고리별 상품 11개

        Map<String, List<ProductDetailsResponse>> response = Map.of(
                "recentProducts", recentProducts,
                "firstItemInCategory", firstItemInCategory
        );

        return ResponseEntity.ok(response);
    }

    // 카테고리 리스트를 가져옴
    private List<String> getCategoryList() {
        return Stream.of(ProductCategory.values())
                .map(ProductCategory::getValue)
                .collect(Collectors.toList());
    }
}
