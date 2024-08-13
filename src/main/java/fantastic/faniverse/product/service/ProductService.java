package fantastic.faniverse.product.service;

import fantastic.faniverse.product.AuctionProduct.domain.AuctionProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProductStatus;
import fantastic.faniverse.product.GeneralProduct.domain.GeneralProduct;
import fantastic.faniverse.product.AuctionProduct.domain.AuctionProduct;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import fantastic.faniverse.product.domain.Product;

import java.util.List;

public interface ProductService {
    void deleteProduct(Long productId);
    List<ProductDetailsResponse> getRecentProducts();
    List<ProductDetailsResponse> getProductsByCategory(List<String> categories);
    Product findOne(Long id);
    List<Product> findAllProducts();
    public List<GeneralProductStatus> getChangeableStatusForGeneralProduct(GeneralProductStatus status);
    public List<AuctionProductStatus> getChangeableStatusForAuctionProduct(AuctionProductStatus status);
    Long addProduct(GeneralProduct generalProduct, Long userId);
    Long addProduct(AuctionProduct auctionProduct, Long userId);
}
