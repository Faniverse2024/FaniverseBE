package fantastic.faniverse.product.dto;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
public class ProductDetailsResponse {
        private String userName;
        private String imageUrl;
        private String title;
        private String category;
        private String content;
        private Double startingPrice;
        private LocalDateTime endDate;
        private String status;
        private Double finalPrice;
        private Long winningUserId;
        private Double price;
        @Getter
        private LocalDateTime createdAt;
        private Product product;

        @Builder
        public ProductDetailsResponse(Product product) {
                this.product = product;
                this.imageUrl = product.getImageUrl();
                this.category = product.getCategory();
                this.content = product.getContent();
                this.userName = product.getSeller().getUsername();
                this.title = product.getTitle();
                if (product instanceof GeneralProduct generalProduct) {
                    this.price = generalProduct.getPrice();
                    this.status = generalProduct.getGeneralProductStatus().getValue();
                }
                else {
                        AuctionProduct auctionProduct = (AuctionProduct) product;
                        this.startingPrice = auctionProduct.getStartingPrice();
                        this.endDate = auctionProduct.getEndDate();
                        this.status = auctionProduct.getAuctionProductStatus().getValue();
                        this.finalPrice = auctionProduct.getFinalPrice();
                }
        }

        public double getPrice() {
                // Product 객체의 실제 타입에 따라 올바른 가격을 반환
                if (product instanceof GeneralProduct) {
                        return ((GeneralProduct) product).getPrice();
                } else if (product instanceof AuctionProduct) {
                        return ((AuctionProduct) product).getStartingPrice();
                } else {
                        throw new IllegalStateException("Unknown product type");
                }
        }
}