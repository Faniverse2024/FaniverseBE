package fantastic.faniverse.product.GeneralProduct.service;

import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductUpdateRequest;
import fantastic.faniverse.product.GeneralProduct.repository.GeneralProductRepository;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductRegisterRequest;
import fantastic.faniverse.product.ProductImage.ImageUploadRequest;
import fantastic.faniverse.Exception.ProductNotFoundException;
import fantastic.faniverse.product.ProductImage.ImageUploadService;
import fantastic.faniverse.product.service.ProductService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Transactional
public class GeneralProductServiceImpl implements GeneralProductService {

    private final UserRepository userRepository;
    private final GeneralProductRepository generalProductRepository;
    private final ImageUploadService imageUploadService;
    private final ProductService productService;

    @Override
    public Long saveGeneralProduct(GeneralProductRegisterRequest request, Long userId) throws IOException {
        // 사용자 검증
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new ProductNotFoundException("사용자를 찾을 수 없습니다"));

        // 이미지 업로드 처리 (이미 컨트롤러에서 처리됨)
       String imageUrl = request.getImageUrl();

        // GeneralProduct 엔티티 생성
        GeneralProduct generalProduct = request.toGeneralProductEntity(imageUrl);

        System.out.println("Image URL before save: " + generalProduct.getImageUrl());

        generalProduct.setSeller(seller);  // seller 설정

        // 상품 저장
        generalProductRepository.save(generalProduct);

        return generalProduct.getId(); // 반환할 값 확인
    }

    @Override
    public Long updateProduct(GeneralProductUpdateRequest request, Long productId, Long userId) throws IOException {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new ProductNotFoundException("사용자를 찾을 수 없습니다"));

        // 기존 상품 정보를 조회
        GeneralProduct existingProduct = generalProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));

        // 이미지 업데이트 처리
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            existingProduct.setImageUrl(request.getImageUrl());
        }
        // 상품 정보 업데이트
        request.updateEntity(existingProduct);
        existingProduct.setSeller(seller); // seller 설정

        // 상품 저장
        generalProductRepository.save(existingProduct);

        return existingProduct.getId();
    }


    @Override
    public void updateGeneralProductStatus(Long id, GeneralProductStatus status) {
        GeneralProduct product = generalProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));

        product.setStatus(status);
        generalProductRepository.save(product);
    }

    @Override
    public GeneralProduct findOne(Long productId) {
        return generalProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));
    }

    @Override
    public String uploadImageToGCS(MultipartFile image) throws IOException {
        // 이미지 업로드 로직 구현
        ImageUploadRequest imageUploadRequest = new ImageUploadRequest();
        imageUploadRequest.setFile(image);
        return imageUploadService.uploadImage(imageUploadRequest);
    }
}
