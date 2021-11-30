package by.ghoncharko.webproject.model.service;

import by.ghoncharko.webproject.entity.Drug;

import java.util.List;

public interface DrugService extends Service<Drug> {
    List<Drug> findAllWhereCountMoreThanZeroByUserId(Integer userId);

    List<Drug> findAllWhereCountMoreThanZero();
}
