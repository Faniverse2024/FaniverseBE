package fantastic.faniverse.wishlist.dto;

import lombok.Getter;
import lombok.Setter;

/*
public class WishlistDto {
    private Long userId;
    private Long productId;

    //Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
*/

@Getter
@Setter
public class WishlistDto {
    private Long userId;
    private Long productId;
}