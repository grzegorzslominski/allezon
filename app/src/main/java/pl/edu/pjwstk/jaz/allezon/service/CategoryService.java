package pl.edu.pjwstk.jaz.allezon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.allezon.repository.CategoryRepository;


@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void addUndefinedCategories(){
        if (categoryRepository.findByName("Undefined").isEmpty()){
            CategoryEntity undefinedCategory = new CategoryEntity();
            undefinedCategory.setName("Undefined");
            categoryRepository.save(undefinedCategory);
        }
    }

}
