package fantastic.faniverse.product.AuctionProduct.service;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.AuctionProduct.dto.AuctionProductRegisterRequest;
import fantastic.faniverse.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Transactional
public interface AuctionProductService {
    Long saveAuctionProduct(AuctionProductRegisterRequest request, Long userId) throws IOException;
    void updateAuctionProductStatus(Long id, AuctionProductStatus status);
    void setAuctionEndDate(Long id, LocalDateTime endDate);
    boolean placeBid(Long auctionProductId, User userId, Double bidAmount);
    boolean cancelBid(Long auctionProductId, Long userId);
    void endAuction(Long auctionProductId);
    boolean confirmPayment(Long productId);
}