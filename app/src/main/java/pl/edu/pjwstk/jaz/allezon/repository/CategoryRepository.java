package pl.edu.pjwstk.jaz.allezon.repository;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.jaz.allezon.DTO.CategoryDTO;
import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class CategoryRepository {
    private final EntityManager entityManager;

    public CategoryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDTO.getName());
        entityManager.persist(categoryEntity);
    }

    public void deleteCategory(CategoryEntity categoryEntity) {
        entityManager.remove(categoryEntity);
    }

    public CategoryEntity findByName(String name) {
        try {
            CategoryEntity categoryEntity = entityManager
                    .createQuery("select se from CategoryEntity se where se.name=:name", CategoryEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return categoryEntity;
        } catch (NoResultException msg) {
            return null;
        }
    }

    public List<CategoryEntity> getCategories() {
        return entityManager
                .createQuery("select se from CategoryEntity se", CategoryEntity.class)
                .getResultList();
    }
}
