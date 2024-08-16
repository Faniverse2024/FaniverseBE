package fantastic.faniverse.wishlist.service;

import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.wishlist.dto.WishlistDto;
import fantastic.faniverse.wishlist.entity.Wishlist;
import fantastic.faniverse.wishlist.repository.WishlistRepository;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import fantastic.faniverse.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<WishlistDto> getWishlistByUserId(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        return wishlists.stream()
                .map(wishlist -> new WishlistDto(wishlist.getId(), wishlist.getUser().getId(), wishlist.getProduct().getId(), wishlist.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public void addWishlistItem(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlistRepository.save(wishlist);
    }

    public void removeWishlistItem(Long userId, Long wishlistId) {
        // User와 Wishlist를 확인하기 위해 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                        .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        // wishlist user 맞는지 확인
        if (!wishlist.getUser().equals(user)) {
            throw new RuntimeException("Wishlist item does not belong to the user");
        }

        // wishlist 삭제
        wishlistRepository.deleteById(wishlistId);
    }
}
