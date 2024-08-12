package fantastic.faniverse.keyword.service;

import fantastic.faniverse.keyword.dto.KeywordDto;
import fantastic.faniverse.keyword.entity.Keyword;
import fantastic.faniverse.keyword.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fantastic.faniverse.product.ProductRepository;
import fantastic.faniverse.product.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private ProductRepository productRepository;

    public Keyword addKeyword(KeywordDto keywordDto) {
        Keyword keyword = new Keyword();
        keyword.setName(keywordDto.getName());
        return keywordRepository.save(keyword);
    }

    public Keyword updateKeyword(Long id, KeywordDto keywordDto) {
        Optional<Keyword> existingKeyword = keywordRepository.findById(id);

        if (existingKeyword.isPresent()) {
            Keyword keyword = existingKeyword.get();
            keyword.setName(keywordDto.getName());
            keyword.setUpdatedAt(LocalDateTime.now());
            return keywordRepository.save(keyword);
        }

        return null;
    }

    public String deleteKeyword(Long id) {
        if (keywordRepository.existsById(id)) {
            keywordRepository.deleteById(id);
            return "Keyword deleted successfully";
        }
        return "Keyword not found";
    }

    public List<Product> searchProductsByKeyword(String keyword) {
        return productRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
}
