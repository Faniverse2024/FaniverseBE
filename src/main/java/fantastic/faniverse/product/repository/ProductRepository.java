package fantastic.faniverse.product.repository;

import fantastic.faniverse.product.domain.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>  {
    List<Product> findByTitleContainingOrContentContaining(String title, String content);
    List<Product> findBySellerId(Long userId);
    List<Product> findTop10ByOrderByCreatedAtDesc(Sort createdAt);
    List<Product> findByTitleContaining(String searchKeyword);
    List<Product> findByCategory(String category);
}
