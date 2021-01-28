package pl.edu.pjwstk.jaz.allezon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.allezon.repository.CategoryRepository;
import pl.edu.pjwstk.jaz.allezon.service.CategoryService;
import pl.edu.pjwstk.jaz.allezon.service.SubcategoryService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;


    @GetMapping("allezon/categories")
    public ResponseEntity<List<CategoryEntity>> getCategory() {
        return new ResponseEntity(categoryRepository.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("allezon/categories")
    public ResponseEntity<String> addCategory(@RequestBody CategoryEntity category) {
        categoryService.addUndefinedCategories();
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            return new ResponseEntity<>("Such an categories exists in the database.", HttpStatus.CONFLICT);
        }
        categoryRepository.save(category);
        return new ResponseEntity("Added categories", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("allezon/categories/{name}")
    public ResponseEntity<String> deleteCategory(@PathVariable String name) {
        Optional categoryEntity = categoryRepository.findByName(name);
        if (categoryEntity.isEmpty()) {
            return new ResponseEntity<>("Such an categories not exists in the database.", HttpStatus.NOT_FOUND);
        }
        subcategoryService.changeCategoriesToUndefined(categoryRepository.findByName(name).get().getId());
        categoryRepository.deleteByName(name);
        return new ResponseEntity("Category removed", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PatchMapping("allezon/categories/edit/{oldName}/{newName}")
    public ResponseEntity<String> editCategory(@PathVariable String oldName , @PathVariable String newName) {
        Optional<CategoryEntity> category = categoryRepository.findByName(oldName);
        if (category.isEmpty()) {
            return new ResponseEntity<>("Such an categories not exists in the database.", HttpStatus.CONFLICT);
        }
        else if (categoryRepository.findByName(newName).isPresent())
        {
            return new ResponseEntity<>("Given name is already taken.", HttpStatus.CONFLICT);
        }
        CategoryEntity categoryEntity = category.get();
        categoryEntity.setName(newName);
        categoryRepository.save(category.get());
        return new ResponseEntity("Category modified", HttpStatus.ACCEPTED);
    }
}
