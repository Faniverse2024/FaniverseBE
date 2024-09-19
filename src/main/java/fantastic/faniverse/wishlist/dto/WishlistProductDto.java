package fantastic.faniverse.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WishlistProductDto {
    private Long productId;
    private String title;
    private String content;
    private String category;
    private String imageUrl;

    // @AllArgsConstructor가 모든 필드를 매개변수로 받는 생성자를 자동으로 생성
}
