package pl.edu.pjwstk.jaz.allezon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.repository.CategoryRepository;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private CategoryRepository categoryRepository;


}
