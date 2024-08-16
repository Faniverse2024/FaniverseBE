package fantastic.faniverse.product.AuctionProduct.domain;

import fantastic.faniverse.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AuctionBid")
public class AuctionBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auction_product_id", nullable = false)
    private AuctionProduct auctionProduct;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double bidAmount;

    @Column(nullable = false)
    private LocalDateTime bidTime;

    @Builder
    public AuctionBid(AuctionProduct auctionProduct, User user, Double bidAmount) {
        this.auctionProduct = auctionProduct;
        this.user = user;
        this.bidAmount = bidAmount;
        this.bidTime = LocalDateTime.now();
    }

}
