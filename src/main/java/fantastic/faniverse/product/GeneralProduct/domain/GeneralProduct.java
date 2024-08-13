package fantastic.faniverse.product.GeneralProduct.domain;
import fantastic.faniverse.product.domain.Product;
import fantastic.faniverse.product.dto.ProductDetailsResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "GeneralProduct")
@Entity
@DiscriminatorValue("general_product")
public class GeneralProduct extends Product {

    @Column(name = "price")
    private double price;

    // 상품 상태 변경
    @Setter
    @Column(name = "generalProductStatus")
    @Enumerated(EnumType.STRING)
    private GeneralProductStatus generalProductStatus;

    //상품 정보 변경
    @Override
    public void update(Product product){
        super.update(product);
        if (product instanceof GeneralProduct) {
            this.price = product.getPrice();
        }
    }

    @Override
    public ProductDetailsResponse toProductDetail() {
        return ProductDetailsResponse.builder()
                .product(this)
                .build();
    }

    public void setStatus(GeneralProductStatus status) {
        this.generalProductStatus = status;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public GeneralProductStatus getStatus() {
        return generalProductStatus;
    }
}