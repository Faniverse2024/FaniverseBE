package fantastic.faniverse.keyword.service;

import fantastic.faniverse.keyword.dto.KeywordDto;
import fantastic.faniverse.keyword.entity.Keyword;
import fantastic.faniverse.keyword.repository.KeywordRepository;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDto;
import fantastic.faniverse.product.repository.ProductRepository;
import fantastic.faniverse.product.service.ProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    // 키워드 추가
    public Keyword addKeyword(KeywordDto keywordDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Keyword keyword = new Keyword();
        keyword.setWord(keywordDto.getWord());
        keyword.setUser(user);
        return keywordRepository.save(keyword);
    }

    // 키워드 수정
    public Keyword updateKeyword(Long id, KeywordDto keywordDto, Long userId) {
        Keyword keyword = keywordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Keyword not found"));

        if (!keyword.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this keyword");
        }

        keyword.setWord(keywordDto.getWord());
        keyword.setUpdatedAt(LocalDateTime.now());
        return keywordRepository.save(keyword);
    }

    // 해당 유저의 키워드 조회 (DTO 반환)
    public List<KeywordDto> findKeywordsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return keywordRepository.findByUser(user).stream()
                .map(keyword -> new KeywordDto(keyword.getId(), keyword.getWord(), userId))
                .collect(Collectors.toList());
    }

    // 키워드 삭제
    public String deleteKeyword(Long keywordId, Long userId) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new RuntimeException("Keyword not found"));

        if (!keyword.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not own this keyword");
        }

        keywordRepository.deleteById(keywordId);
        return "Keyword deleted successfully";
    }

    // 키워드로 상품 찾기 (DTO 반환)
    public List<ProductDto> findProductsByKeyword(String keyword) {
        // 엔티티 리스트 조회
        List<Product> products = productRepository.findByTitleContaining(keyword);

        // 엔티티 리스트 -> Dto 리스트로 변환
        return products.stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getTitle(),
                        product.getContent(),
                        product.getCategory(),
                        product.getImageUrl(),
                        product.getCreatedAt(),
                        product.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 유저의 키워드에 따라 상품 찾기 (DTO 반환)
    public Map<String, List<ProductDto>> findProductsByUserKeywords(Long userId) {
        List<KeywordDto> keywordDtos = findKeywordsByUserId(userId);  // KeywordDto로 변경
        Map<String, List<ProductDto>> keywordToProductsMap = new HashMap<>();

        for (KeywordDto keywordDto : keywordDtos) {
            String keywordValue = keywordDto.getWord();
            List<Product> products = productRepository.findByTitleContaining(keywordValue);

            List<ProductDto> productDtos = products.stream()
                    .map(product -> new ProductDto(
                            product.getId(),
                            product.getTitle(),
                            product.getContent(),
                            product.getCategory(),
                            product.getImageUrl(),
                            product.getCreatedAt(),
                            product.getUpdatedAt()
                    ))
                    .collect(Collectors.toList());

            keywordToProductsMap.put(keywordValue, productDtos);
        }

        return keywordToProductsMap;
    }
}
