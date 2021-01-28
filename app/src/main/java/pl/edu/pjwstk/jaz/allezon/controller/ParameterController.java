package pl.edu.pjwstk.jaz.allezon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.jaz.allezon.entity.CategoryEntity;
import pl.edu.pjwstk.jaz.allezon.entity.ParameterEntity;
import pl.edu.pjwstk.jaz.allezon.repository.ParameterRepository;
import pl.edu.pjwstk.jaz.allezon.service.ParameterService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ParameterController {

    private final ParameterRepository parameterRepository;
    private final ParameterService parameterService;

    // lista parametrów (tylko zalogowani)
    @GetMapping("allezon/parameter")
    public ResponseEntity<List<CategoryEntity>> getParameters() {
        return new ResponseEntity(parameterRepository.findAll(), HttpStatus.OK);
    }


    //dodawanie parametrów (tylko admin)
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("allezon/parameter")
    public ResponseEntity<String> addParameter(@RequestBody ParameterEntity parameter) {
        parameterService.addUndefinedParameter();
        if (parameterRepository.findByName(parameter.getName())!=null) {
            return new ResponseEntity<>("Such an parameter exists in the database", HttpStatus.CONFLICT);
        }
        parameterRepository.save(parameter);
        return new ResponseEntity("Added parameter", HttpStatus.CREATED);
    }


    //usuwanie parametrów (tylko admin)
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("allezon/parameter/{parameterName}")
    public ResponseEntity<String> deleteParameter(@PathVariable String parameterName) {
        if (parameterRepository.findByName(parameterName)==null) {
            return new ResponseEntity<>("Such an parameter not exists in the database.", HttpStatus.NOT_FOUND);
        }
        parameterService.changeParameterToUndefined(parameterRepository.findByName(parameterName).getId());
        parameterRepository.delete(parameterRepository.findByName(parameterName));
        return new ResponseEntity("Parameter removed", HttpStatus.OK);
    }


    //zmiana nazwy parametrów (tylko admin)
    @PreAuthorize("hasAuthority('admin')")
    @PatchMapping("allezon/parameter/edit/{oldName}/{newName}")
    public ResponseEntity<String> editParameter(@PathVariable String oldName , @PathVariable String newName) {
        ParameterEntity parameter = parameterRepository.findByName(oldName);
        if (parameter==null) {
            return new ResponseEntity<>("Such an parameter not exists in the database.", HttpStatus.CONFLICT);
        }
        else if (parameterRepository.findByName(newName)!=null)
        {
            return new ResponseEntity<>("Given name is already taken.", HttpStatus.CONFLICT);
        }
        parameter.setName(newName);
        parameterRepository.save(parameter);
        return new ResponseEntity("Parameter modified", HttpStatus.ACCEPTED);
    }



}
