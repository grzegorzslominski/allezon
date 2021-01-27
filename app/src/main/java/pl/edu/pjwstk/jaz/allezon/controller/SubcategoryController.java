package pl.edu.pjwstk.jaz.allezon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.allezon.entity.SubcategoryEntity;
import pl.edu.pjwstk.jaz.allezon.repository.CategoryRepository;
import pl.edu.pjwstk.jaz.allezon.repository.SubcategoryRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SubcategoryController {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;

    @GetMapping("allezon/subcategories")
    public ResponseEntity<List<CategoryEntity>> getCategory() {
        return new ResponseEntity(subcategoryRepository.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("allezon/subcategories")
    public ResponseEntity<String> addCategory(@RequestBody SubcategoryEntity subcategory) {
        if (subcategoryRepository.findByName(subcategory.getName()).isPresent()) {
            return new ResponseEntity<>("Such an subcategories exists in the database", HttpStatus.CONFLICT);
        }
        else if(categoryRepository.findById(subcategory.getCategoryId()).isEmpty()) {
            return new ResponseEntity<>("Given category does not exist", HttpStatus.CONFLICT);
        }
        subcategoryRepository.save(subcategory);
        return new ResponseEntity("Added subcategories", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("allezon/subcategories/{name}")
    public ResponseEntity<String> deleteCategory(@PathVariable String name) {
        if (subcategoryRepository.findByName(name).isEmpty()) {
            return new ResponseEntity<>("Such an categories not exists in the database", HttpStatus.NOT_FOUND);
        }
        SubcategoryEntity subcategoryEntity = subcategoryRepository.findByName(name).get();
        subcategoryRepository.delete(subcategoryEntity);
        return new ResponseEntity("Category removed", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PatchMapping("allezon/subcategories/edit/{oldName}/{newName}")
    public ResponseEntity<String> editSubcategory(@PathVariable String oldName , @PathVariable String newName) {
        Optional<SubcategoryEntity> subcategory = subcategoryRepository.findByName(oldName);
        if (subcategory.isEmpty()) {
            return new ResponseEntity<>("Such an subcategories not exists in the database", HttpStatus.CONFLICT);
        }
        else if (subcategoryRepository.findByName(newName).isPresent())
        {
            return new ResponseEntity<>("Given name is already taken", HttpStatus.CONFLICT);
        }
        SubcategoryEntity subcategoryEntity = subcategory.get();
        subcategoryEntity.setName(newName);
        subcategoryRepository.save(subcategory.get());
        return new ResponseEntity("Subcategory modified", HttpStatus.ACCEPTED);
    }
}
