package pl.edu.pjwstk.jaz.allezon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import pl.edu.pjwstk.jaz.allezon.DTO.CategoryDTO;
import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.allezon.repository.CategoryRepository;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("allezon/categories")
    public ResponseEntity<List<CategoryEntity>> getCategory() {
        return new ResponseEntity(categoryRepository.getCategories(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("allezon/categories")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        if (categoryRepository.findByName(categoryDTO.getName()) != null) {
            return new ResponseEntity<>("Such an categories exists in the database.", HttpStatus.CONFLICT);
        }
        categoryRepository.addCategory(categoryDTO);
        return new ResponseEntity("Added categories", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("allezon/categories")
    public ResponseEntity<String> deleteCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryDTO.getName());
        if (categoryEntity == null) {
            return new ResponseEntity<>("Such an categories not exists in the database.", HttpStatus.CONFLICT);
        }
        categoryRepository.deleteCategory(categoryEntity);
        return new ResponseEntity("Deleted categories", HttpStatus.NO_CONTENT);
    }
}
