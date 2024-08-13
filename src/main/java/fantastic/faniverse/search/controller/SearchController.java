package fantastic.faniverse.search.controller;

import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    // 검색 결과
    @GetMapping("/results")
    public ResponseEntity<List<ProductDetailsResponse>> searchProducts(
            @RequestParam String searchWord,
            @RequestParam(value = "sortBy", defaultValue = "latest") String sortBy,
            @RequestParam Long userId) {
        List<ProductDetailsResponse> products = searchService.searchProducts(userId, searchWord);
        searchService.addRecentSearchToUser(userId, searchWord);
        return ResponseEntity.ok(products);
    }

    // 최근 검색어 조회
    @GetMapping("/recent")
    public ResponseEntity<List<String>> getRecentSearchesFromUser(@RequestParam Long userId) {
        List<String> recentSearchWords = searchService.getRecentSearchesFromUser(userId);
        return ResponseEntity.ok(recentSearchWords);
    }


    // 최근 검색어 추가
    @PostMapping("/recent")
    public ResponseEntity<Void> addRecentSearchToUser(@RequestParam Long userId, @RequestParam String searchWord) {
        searchService.addRecentSearchToUser(userId, searchWord);
        return ResponseEntity.ok().build();
    }

    // 상품 필터링 및 정렬
    @GetMapping("/results/filter")
    public ResponseEntity<List<ProductDetailsResponse>> filterProducts(
            @RequestParam String keyword,
            @RequestParam Long userId,
            @RequestParam(value = "sortBy", defaultValue = "latest") String sortBy) {
        List<ProductDetailsResponse> products = searchService.filterProducts(keyword, sortBy, userId);
        return ResponseEntity.ok(products);
    }

}
