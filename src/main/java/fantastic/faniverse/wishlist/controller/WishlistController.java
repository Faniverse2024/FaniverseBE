package fantastic.faniverse.wishlist.controller;

import fantastic.faniverse.wishlist.dto.WishlistDto;
import fantastic.faniverse.wishlist.entity.Wishlist;
import fantastic.faniverse.wishlist.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<String> addWishlist(@RequestBody WishlistDto wishlistDto) {
        String response = wishlistService.addWishlist(wishlistDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<String> removeWishlist(@PathVariable Long userId, @PathVariable Long productId) {
        String response = wishlistService.removeWishlist(userId, productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getWishlist(@PathVariable Long userId) {
        List<Wishlist> wishlist = wishlistService.getWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }
}
