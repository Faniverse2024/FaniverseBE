package fantastic.faniverse.keyword.controller;

import fantastic.faniverse.keyword.dto.KeywordDto;
import fantastic.faniverse.keyword.entity.Keyword;
import fantastic.faniverse.keyword.service.KeywordService;
import fantastic.faniverse.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    @PostMapping
    public ResponseEntity<Keyword> addKeyword(@RequestBody KeywordDto keywordDto) {
        Keyword keyword = keywordService.addKeyword(keywordDto);
        return ResponseEntity.ok(keyword);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Keyword> updatedKeyword(@PathVariable Long id, @RequestBody KeywordDto keywordDto) {
        Keyword keyword = keywordService.updateKeyword(id, keywordDto);
        if (keyword != null) {
            return ResponseEntity.ok(keyword);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKeyword(@PathVariable Long id) {
        String response = keywordService.deleteKeyword(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByKeyword(@RequestParam String keyword) {
        List<Product> products = keywordService.searchProductsByKeyword(keyword);
        return ResponseEntity.ok(products);
    }
}
