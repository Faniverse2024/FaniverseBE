package fantastic.faniverse.product.AuctionProduct.controller;

import fantastic.faniverse.product.AuctionProduct.service.AuctionProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class AuctionProductController {
    private final AuctionProductService auctionProductService;
    private final UserRepository userRepository;

    // 경매 종료 날짜 지정
    @PutMapping("/{productId}/end-date")
    public ResponseEntity<String> setEndDate(@PathVariable Long productId, @RequestBody Map<String, String> request) {
        String endDateStr = request.get("endDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime endDate = LocalDate.parse(endDateStr, formatter).atStartOfDay();

        auctionProductService.setAuctionEndDate(productId, endDate);

        return ResponseEntity.ok("End date set to: " + endDate);
    }

    // 경매 입찰 등록
    /*
    @PostMapping("/{productId}/bids")
    public ResponseEntity<String> placeBid(@PathVariable Long productId, @RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        Double bidAmount = Double.parseDouble(request.get("bidAmount").toString());

        boolean isBidPlaced = auctionProductService.placeBid(productId, userId, bidAmount);
        if (isBidPlaced) {
            return ResponseEntity.ok("Bid placed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to place bid.");
        }
    } */
    // 경매 입찰 등록
    @PostMapping("/{productId}/bids")
    public ResponseEntity<String> placeBid(@PathVariable Long productId, @RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        Double bidAmount = Double.parseDouble(request.get("bidAmount").toString());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isBidPlaced = auctionProductService.placeBid(productId, user, bidAmount);
        if (isBidPlaced) {
            return ResponseEntity.ok("Bid placed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to place bid.");
        }
    }

    // 경매 입찰 취소
    @DeleteMapping("/{productId}/bids/{userId}")
    public ResponseEntity<String> cancelBid(@PathVariable Long productId, @PathVariable Long userId) {
        boolean isBidCancelled = auctionProductService.cancelBid(productId, userId);
        if (isBidCancelled) {
            return ResponseEntity.ok("Bid cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel bid.");
        }
    }

    // 경매 종료
    @PutMapping("/{productId}/end-auction")
    public ResponseEntity<String> endAuction(@PathVariable Long productId) {
        auctionProductService.endAuction(productId);
        return ResponseEntity.ok("Auction ended successfully.");
    }

    // 경매 금액 지불 여부 확인
    @PutMapping("/{productId}/confirm-payment")
    public ResponseEntity<String> confirmPayment(@PathVariable Long productId) {
        boolean isPaymentConfirmed = auctionProductService.confirmPayment(productId);
        if (isPaymentConfirmed) {
            return ResponseEntity.ok("Payment confirmed.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to confirm payment.");
        }
    }
}
