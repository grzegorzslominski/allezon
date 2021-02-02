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
import pl.edu.pjwstk.jaz.allezon.service.AuctionService;
import pl.edu.pjwstk.jaz.allezon.service.SubcategoryService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SubcategoryController {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final SubcategoryService subcategoryService;
    private final AuctionService auctionService;


    //lista wszystkich podakategori (tylko zalogiwani)
    @GetMapping("allezon/subcategories")
    public ResponseEntity<List<CategoryEntity>> getCategory() {
        return new ResponseEntity(subcategoryRepository.findAll(), HttpStatus.OK);
    }


    //doadawanie podkategori (tylko admin)
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("allezon/subcategories")
    public ResponseEntity<String> addSubcategory(@RequestBody SubcategoryEntity subcategory) {
        subcategoryService.addUndefinedSubcategories();
        if (subcategoryRepository.findByName(subcategory.getName()).isPresent()) {
            return new ResponseEntity<>("Such an subcategories exists in the database", HttpStatus.CONFLICT);
        }
        else if(categoryRepository.findById(subcategory.getCategoryId()).isEmpty()) {
            return new ResponseEntity<>("Given category does not exist", HttpStatus.CONFLICT);
        }
        subcategoryRepository.save(subcategory);
        return new ResponseEntity("Added subcategories", HttpStatus.CREATED);
    }
    //usuwanie podkategori (tylko admin)
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("allezon/subcategories/{name}")
    public ResponseEntity<String> deleteSubcategory(@PathVariable String name) {
        if (subcategoryRepository.findByName(name).isEmpty()) {
            return new ResponseEntity<>("Such an categories not exists in the database", HttpStatus.NOT_FOUND);
        }
         auctionService.changeSubcategoriesToUndefined(subcategoryRepository.findByName(name).get().getId());
       // auctionService.changeSubcategoriesToUndefined(subcategoryRepository.findByName(name).get().getId());
        SubcategoryEntity subcategoryEntity = subcategoryRepository.findByName(name).get();
        subcategoryRepository.delete(subcategoryEntity);
        return new ResponseEntity("Category removed", HttpStatus.OK);
    }
    //edycja podkategori (tylko admin)
    @PreAuthorize("hasAuthority('admin')")
    @PatchMapping("allezon/subcategories/edit/{nameSubcategoryToChange}")
    public ResponseEntity<String> editSubcategory(@PathVariable String nameSubcategoryToChange ,@RequestBody SubcategoryEntity subcategoryEntity) {
        Optional<SubcategoryEntity> subcategory = subcategoryRepository.findByName(nameSubcategoryToChange);
        if (subcategory.isEmpty()) {
            return new ResponseEntity<>("Such an subcategories not exists in the database", HttpStatus.CONFLICT);
        }
        else if (subcategoryRepository.findByName(subcategoryEntity.getName()).isPresent())
        {
            return new ResponseEntity<>("Given name is already taken", HttpStatus.CONFLICT);
        }
        if(subcategoryEntity.getName()!=null)
            subcategory.get().setName(subcategoryEntity.getName());
        if(subcategoryEntity.getCategoryId()!=null){
            subcategory.get().setCategoryId(subcategoryEntity.getCategoryId());
        }
        subcategoryRepository.save(subcategory.get());
        return new ResponseEntity("Subcategory modified", HttpStatus.ACCEPTED);
    }
}
