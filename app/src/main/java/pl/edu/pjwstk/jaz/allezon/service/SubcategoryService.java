package pl.edu.pjwstk.jaz.allezon.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.entity.SubcategoryEntity;
import pl.edu.pjwstk.jaz.allezon.repository.CategoryRepository;
import pl.edu.pjwstk.jaz.allezon.repository.SubcategoryRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;



    public void addUndefinedSubcategories(){
        if (subcategoryRepository.findByName("Undefined").isEmpty()){
            SubcategoryEntity undefinedSubcategory = new SubcategoryEntity();
            undefinedSubcategory.setName("Undefined");
            if(categoryRepository.findByName("Undefined").isEmpty()){
                categoryService.addUndefinedCategories();
            }
            undefinedSubcategory.setCategoryId(categoryRepository.findByName("Undefined").get().getId());
            subcategoryRepository.save(undefinedSubcategory);
        }
    }

    public  void changeCategoriesToUndefined(Long categoryId){
     List <SubcategoryEntity> subcategoryEntityList= subcategoryRepository.findAllByCategoryId(categoryId);
        for (int i = 0; i < subcategoryEntityList.size(); i++) {
            subcategoryEntityList.get(i).setCategoryId(categoryRepository.findByName("Undefined").get().getId());
        }
    }


}
