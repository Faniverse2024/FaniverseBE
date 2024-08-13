package fantastic.faniverse.product.AuctionProduct.service;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.AuctionProduct.dto.AuctionProductRegisterRequest;
import fantastic.faniverse.product.AuctionProduct.repository.AuctionProductRepository;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.ProductImage.ImageUploadRequest;
import fantastic.faniverse.product.ProductImage.ImageUploadService;
import fantastic.faniverse.Exception.ProductNotFoundException;
import fantastic.faniverse.product.service.ProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuctionProductServiceImpl implements AuctionProductService {

    private final UserRepository userRepository;
    private final AuctionProductRepository auctionProductRepository;
    private final ImageUploadService imageUploadService;
    private final ProductService productService;

    @Override
    public Long saveAuctionProduct(AuctionProductRegisterRequest request, Long userId) throws IOException {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 이미지 업로드
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            ImageUploadRequest imageUploadRequest = new ImageUploadRequest();
            imageUploadRequest.setFile(request.getImage());
            imageUrl = imageUploadService.uploadImage(imageUploadRequest);
        }

        // AuctionProduct 엔티티 생성
        AuctionProduct auctionProduct = request.toAuctionProductEntity(imageUrl);
        return productService.addProduct(auctionProduct, userId);
    }

    @Override
    public void updateAuctionProductStatus(Long id, AuctionProductStatus status) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));
        auctionProduct.setAuctionStatus(status);
        auctionProductRepository.save(auctionProduct);
    }

    @Override
    public void setAuctionEndDate(Long id, LocalDateTime endDate) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));
        auctionProduct.setEndDate(endDate);
        auctionProductRepository.save(auctionProduct);
    }

    @Override
    public boolean placeBid(Long auctionProductId, User user, Double bidAmount) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(auctionProductId)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));

        boolean isBidPlaced = auctionProduct.placeBid(user, bidAmount);
        auctionProductRepository.save(auctionProduct);

        return isBidPlaced;
    }

    @Override
    public boolean cancelBid(Long auctionProductId, Long userId) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(auctionProductId)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));

        // 사용자 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (auctionProduct.isAuctionEnded()) {
            return false; // 경매가 이미 종료된 경우는 입찰 취소 불가
        }

        boolean isBidCancelled = auctionProduct.cancelBid(user);
        auctionProductRepository.save(auctionProduct);

        return isBidCancelled;
    }

    @Override
    public void endAuction(Long auctionProductId) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(auctionProductId)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));

        auctionProduct.endAuction();
        auctionProductRepository.save(auctionProduct);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAuctionStatus() {
        List<AuctionProduct> auctionProducts = auctionProductRepository.findByAuctionProductStatus(AuctionProductStatus.SOLD);

        for (AuctionProduct auctionProduct : auctionProducts) {
            if (auctionProduct.getUpdatedAt().plusDays(1).isBefore(LocalDateTime.now()) && !isPaymentConfirmed(auctionProduct)) {
                auctionProduct.selectNextHighestBidder();
                auctionProductRepository.save(auctionProduct);
            }
        }
    }

    private boolean isPaymentConfirmed(AuctionProduct auctionProduct) {
        return auctionProduct.getAuctionProductStatus() == AuctionProductStatus.SOLD;
    }

    public boolean confirmPayment(Long productId) {
        AuctionProduct auctionProduct = auctionProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Auction product not found"));

        if (auctionProduct.getAuctionProductStatus() == AuctionProductStatus.SOLD) {
            return false;
        }

        auctionProduct.setAuctionStatus(AuctionProductStatus.SOLD);
        auctionProductRepository.save(auctionProduct);

        return true;
    }
}
