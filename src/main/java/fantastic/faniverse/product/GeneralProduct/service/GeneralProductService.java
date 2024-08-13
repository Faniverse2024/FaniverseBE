package fantastic.faniverse.product.GeneralProduct.service;

import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.GeneralProduct.dto.GeneralProductRegisterRequest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Transactional
public interface GeneralProductService {
    Long saveGeneralProduct(GeneralProductRegisterRequest request, Long userId) throws IOException;
    void updateProduct(GeneralProductRegisterRequest request, Long userId, GeneralProduct product) throws IOException;
    void updateGeneralProductStatus(Long id, GeneralProductStatus status);
    GeneralProduct findOne(Long productId);
}
