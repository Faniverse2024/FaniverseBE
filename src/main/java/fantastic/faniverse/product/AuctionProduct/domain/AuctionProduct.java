package fantastic.faniverse.product.AuctionProduct.domain;

import fantastic.faniverse.Exception.NoBidderException;
import fantastic.faniverse.user.entity.User;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "AuctionProduct")
@Entity
@DiscriminatorValue("auction_product")
public class AuctionProduct extends Product {

    @Column(name = "auctionProductStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionProductStatus auctionProductStatus;

    @Column(name="startingPrice", nullable = false)
    private double startingPrice;

    @Nullable
    @Column(name="finalPrice")
    private double finalPrice;

    @OneToOne
    @Nullable
    @JoinColumn(name = "winningBid")
    private AuctionBid winningBid;

    @Column(name="endDate", nullable = false)
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "auctionProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuctionBid> bids = new ArrayList<>();

    @Override
    public ProductDetailsResponse toProductDetail() {
        return ProductDetailsResponse.builder()
                .product(this)
                .build();
    }

    public void setAuctionStatus(AuctionProductStatus status) {
        this.auctionProductStatus = status;
    }

    @Override
    public double getPrice() {
        return startingPrice;
    }

    // 입찰 추가
    public boolean placeBid(User user, Double bidAmount) {
        AuctionBid newBid = new AuctionBid(this, user, bidAmount);

        // 중복된 사용자의 입찰은 허용하지 않음
        boolean userAlreadyBid = bids.stream()
                .anyMatch(bid -> bid.getUser().equals(user));
        if (userAlreadyBid) {
            return false;
        }

        bids.add(newBid);
        return true;
    }

    //경매 종료 여부 확인
    public boolean isAuctionEnded() {
        return LocalDateTime.now().isAfter(endDate);
    }

    // 입찰 취소
    public boolean cancelBid(User user) {
        return bids.removeIf(bid -> bid.getUser().equals(user));
    }

    // bids 리스트에서 최고 입찰 정보 반환
    public Optional<AuctionBid> findHighestBid() {
        return Optional.ofNullable(winningBid)
                .or(() -> bids.stream()
                        .max(Comparator.comparingDouble(AuctionBid::getBidAmount)));
    }

    // 최고 입찰자 선정 - 입찰자가 없을 경우 추가
    public void endAuction() {
        if (!isAuctionEnded()) {
            return;
        }
        if (winningBid != null) {
            this.auctionProductStatus = AuctionProductStatus.Pending;
        } else {
            this.auctionProductStatus = AuctionProductStatus.FAIL;
            throw new NoBidderException();
        }
    }

    // 다음 최고 입찰자 선정
    public void selectNextHighestBidder() {
        Optional<AuctionBid> highestBid = bids.stream()
                .filter(bid -> bid.getBidAmount() > (winningBid != null ? winningBid.getBidAmount() : Double.MIN_VALUE))
                .max(Comparator.comparing(AuctionBid::getBidAmount));

        if (highestBid.isPresent()) {
            winningBid = highestBid.get();
        } else {
            this.auctionProductStatus = AuctionProductStatus.FAIL;
            throw new NoBidderException(); // 더 이상 높은 입찰이 없음
        }
    }
}
