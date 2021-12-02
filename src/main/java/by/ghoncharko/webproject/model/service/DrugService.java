package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;

import java.util.List;

public interface DrugService extends Service<Drug> {
    List<Drug> findAllWhereCountMoreThanZeroByUserId(Integer userId);

    List<Drug> findAllWhereCountMoreThanZero();

    boolean deleteByDrugId(Integer drugId);

    boolean update(Integer drugId, String drugName, Boolean drugNeedRecipe, Integer drugCount, Double drugPrice, String drugDescription , String drugProducerName);
}
