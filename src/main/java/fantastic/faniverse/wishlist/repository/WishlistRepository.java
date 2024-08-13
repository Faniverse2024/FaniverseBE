package fantastic.faniverse.wishlist.repository;

import fantastic.faniverse.wishlist.entity.Wishlist;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);
    Wishlist findByUserAndProduct(User user, Product product);

    List<Wishlist> findByUser(User user);
}
