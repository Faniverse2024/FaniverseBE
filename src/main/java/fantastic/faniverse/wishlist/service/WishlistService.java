package fantastic.faniverse.wishlist.service;

import fantastic.faniverse.user.repository.UserRepository;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.product.repository.ProductRepository;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.wishlist.dto.WishlistDto;
import fantastic.faniverse.wishlist.entity.Wishlist;
import fantastic.faniverse.wishlist.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public String addWishlist(WishlistDto wishlistDto) {
        User user = userRepository.findById(wishlistDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(wishlistDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist existingWishlist = wishlistRepository.findByUserAndProduct(user, product);

        if (existingWishlist != null) {
            return "Product already in wishlist";
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);
        return "Product added to wishlist";
    }

    public String removeWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserAndProduct(user, product);

        if (wishlist == null) {
            return "Product not found in wishlist";
        }

        wishlistRepository.delete(wishlist);
        return "Product removed from wishlist";
    }

    public List<Wishlist> getWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return wishlistRepository.findByUser(user);
    }
}