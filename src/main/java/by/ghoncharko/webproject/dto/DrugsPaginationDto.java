package by.ghoncharko.webproject.dto;

import by.ghoncharko.webproject.entity.Drug;

import java.util.List;
import java.util.Objects;

public class DrugsPaginationDto {
    private final List<Drug> drugList;
    private final Integer countPages;

    private DrugsPaginationDto(Builder builder) {
        this.drugList = builder.drugList;
        this.countPages = builder.countPages;
    }

    public List<Drug> getDrugList() {
        return drugList;
    }

    public Integer getCountPages() {
        return countPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugsPaginationDto that = (DrugsPaginationDto) o;

        if (!Objects.equals(drugList, that.drugList)) return false;
        return Objects.equals(countPages, that.countPages);
    }

    @Override
    public String toString() {
        return "DrugsPaginationDto{" +
                "drugList=" + drugList +
                ", countDrugs=" + countPages +
                '}';
    }

    @Override
    public int hashCode() {
        int result = drugList != null ? drugList.hashCode() : 0;
        result = 31 * result + (countPages != null ? countPages.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private List<Drug> drugList;
        private Integer countPages;

        public Builder wuthDrugList(List<Drug> drugList) {
            this.drugList = drugList;
            return this;
        }

        public Builder withCountPages(Integer countPages) {
            this.countPages = countPages;
            return this;
        }

        public DrugsPaginationDto build() {
            return new DrugsPaginationDto(this);
        }
    }
}
