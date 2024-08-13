package fantastic.faniverse.product.GeneralProduct.service;

import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductRegisterRequest;
import fantastic.faniverse.product.GeneralProduct.repository.GeneralProductRepository;
import fantastic.faniverse.product.ProductImage.ImageUploadRequest;
import fantastic.faniverse.product.ProductImage.ImageUploadService;
import fantastic.faniverse.Exception.ProductNotFoundException;
import fantastic.faniverse.product.service.ProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@NoArgsConstructor(force = true)
@Transactional
@RequiredArgsConstructor
@Service
public class GeneralProductServiceImpl implements GeneralProductService {
    @Autowired
    private final UserRepository userRepository;
    private final GeneralProductRepository generalProductRepository;
    private final ImageUploadService imageUploadService;
    private final ProductService productService;

    //일반 상품 저장
    @Override
    public Long saveGeneralProduct(GeneralProductRegisterRequest request, Long userId) throws IOException {
        User seller = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 이미지 업로드
        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            ImageUploadRequest imageUploadRequest = new ImageUploadRequest();
            imageUploadRequest.setFile(request.getImage());
            imageUrl = imageUploadService.uploadImage(imageUploadRequest);
        }

        // GeneralProduct 엔티티 생성
        GeneralProduct generalProduct = request.toGeneralProductEntity(imageUrl);
        return productService.addProduct(generalProduct, userId);

    }

    //상품 수정 - 일반 상품
    @Override
    public void updateProduct(GeneralProductRegisterRequest request, Long userId, GeneralProduct product) throws IOException {

        // 이미지 수정
        String imageUrl = product.getImageUrl(); // 기존 이미지 URL을 기본값으로 설정
        // 요청에 이미지가 있는 경우만 이미지 업로드를 수행
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            ImageUploadRequest imageUploadRequest = new ImageUploadRequest();
            imageUploadRequest.setFile(request.getImage());
            // 새로운 이미지를 업로드하고 URL을 업데이트
            imageUrl = imageUploadService.uploadImage(imageUploadRequest);
        }
        GeneralProduct updatedProduct = request.toGeneralProductEntity(imageUrl);
        // 기존 상품 정보에 업데이트된 내용 반영
        product.update(updatedProduct);
        // 업데이트된 상품 정보 저장
        generalProductRepository.save(product);
    }


    //일반 상품 판매 상태 변경
    @Override
    public void updateGeneralProductStatus(Long id, GeneralProductStatus status) {
        GeneralProduct product = generalProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));
        product.setStatus(status);
        generalProductRepository.save(product);
    }

    //일반 상품 중 상품 1개 찾기
    @Override
    public GeneralProduct findOne(Long productId) {
        return generalProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));
    }
}
