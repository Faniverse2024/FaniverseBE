package fantastic.faniverse.product.ProductCategory.domain;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
@Getter
@AllArgsConstructor
public enum ProductCategory {

    ROOT("카테고리", null),
        MUSIC("음악", ROOT),
            GIRLGROUP("걸그룹", MUSIC),
            BOYROUP("보이그룹", MUSIC),
            SOLO("솔로", MUSIC),
            TROT("트로트", MUSIC),
            ETC("기타", MUSIC),
        SPORTS("스포츠", ROOT),
            FOOTBALL("축구", SPORTS),
            BASKETBALL("농구", SPORTS),
            VOLLEYBALL("배구", SPORTS),
            BASEBALL("야구", SPORTS),
        ANI("애니", ROOT),
        GAME("게임", ROOT);

    private final String title;
    private String value;
    private final ProductCategory parentCategory;
    private final List<ProductCategory> childCategories;

    ProductCategory(String title, ProductCategory parentCategory) {
        this.childCategories = new ArrayList<>();
        this.title = title;
        this.parentCategory = parentCategory;
        if(parentCategory != null) {
            parentCategory.childCategories.add(this);
        }
    }

    private static final List<ProductCategory> ALL_CATEGORIES = new ArrayList<>();

    static {
        for (ProductCategory category : ProductCategory.values()) {
            ALL_CATEGORIES.add(category);
        }
    }

    public static List<ProductCategory> getAllCategories() {
        return Collections.unmodifiableList(ALL_CATEGORIES);
    }
}