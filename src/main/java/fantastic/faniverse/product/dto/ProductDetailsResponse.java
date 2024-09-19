package fantastic.faniverse.product.dto;

import fantastic.faniverse.Exception.NoBidderException;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionBid;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Getter
@AllArgsConstructor
@Setter
@Builder
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
        private Double price;
        private LocalDateTime createdAt;
        private AuctionBid winningBid;

        //@Builder
        public ProductDetailsResponse(Product product) {
                this.imageUrl = product.getImageUrl();
                this.category = product.getCategory();
                this.content = product.getContent();
                this.userName = product.getSeller().getUsername();
                this.title = product.getTitle();

                if (product instanceof GeneralProduct generalProduct) {
                        this.price = generalProduct.getPrice();
                        this.status = generalProduct.getGeneralProductStatus().getValue();
                } else if (product instanceof AuctionProduct auctionProduct) {
                        this.startingPrice = auctionProduct.getStartingPrice();
                        this.endDate = auctionProduct.getEndDate();
                        this.status = auctionProduct.getAuctionProductStatus().getValue();
                        this.finalPrice = auctionProduct.getFinalPrice();
                        this.winningBid = auctionProduct.getWinningBidNow(auctionProduct);
                }
        }

        public double getPrice() {
                return this.price != null ? this.price : (this.startingPrice != null ? this.startingPrice : 0.0);
        }

}
