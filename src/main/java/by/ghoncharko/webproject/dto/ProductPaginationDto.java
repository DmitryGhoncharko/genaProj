package by.ghoncharko.webproject.dto;

import by.ghoncharko.webproject.entity.Product;

import java.util.List;
import java.util.Objects;

public class ProductPaginationDto {
    private final List<Product> productList;
    private final Integer countPages;

    private ProductPaginationDto(Builder builder) {
        this.productList = builder.productList;
        this.countPages = builder.countPages;
    }

    public List<Product> getDrugList() {
        return productList;
    }

    public Integer getCountPages() {
        return countPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductPaginationDto that = (ProductPaginationDto) o;

        if (!Objects.equals(productList, that.productList)) return false;
        return Objects.equals(countPages, that.countPages);
    }

    @Override
    public String toString() {
        return "DrugsPaginationDto{" +
                "drugList=" + productList +
                ", countDrugs=" + countPages +
                '}';
    }

    @Override
    public int hashCode() {
        int result = productList != null ? productList.hashCode() : 0;
        result = 31 * result + (countPages != null ? countPages.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private List<Product> productList;
        private Integer countPages;

        public Builder wuthDrugList(List<Product> productList) {
            this.productList = productList;
            return this;
        }

        public Builder withCountPages(Integer countPages) {
            this.countPages = countPages;
            return this;
        }

        public ProductPaginationDto build() {
            return new ProductPaginationDto(this);
        }
    }
}
