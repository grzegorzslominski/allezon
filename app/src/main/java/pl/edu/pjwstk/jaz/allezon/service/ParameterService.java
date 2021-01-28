package pl.edu.pjwstk.jaz.allezon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.jaz.allezon.entity.AuctionParameterEntity;
import pl.edu.pjwstk.jaz.allezon.entity.ParameterEntity;
import pl.edu.pjwstk.jaz.allezon.entity.SubcategoryEntity;
import pl.edu.pjwstk.jaz.allezon.repository.AuctionParameterRepository;
import pl.edu.pjwstk.jaz.allezon.repository.ParameterRepository;

import java.util.List;


@AllArgsConstructor
@Service
public class ParameterService {
    private final ParameterRepository parameterRepository;
    private final AuctionParameterRepository auctionParameterRepository;


    public void addUndefinedParameter(){
        if (parameterRepository.findByName("Undefined")==null){
            ParameterEntity undefinedParameter = new ParameterEntity();
            undefinedParameter.setName("Undefined");
            parameterRepository.save(undefinedParameter);
        }
    }

    public  void changeParameterToUndefined(Long parameterId){
        List<AuctionParameterEntity> auctionParameterEntityList= auctionParameterRepository.findAllByParameterId(parameterId);
        for (int i = 0; i < auctionParameterEntityList.size(); i++) {
            auctionParameterEntityList.get(i).setParameterId(parameterRepository.findByName("Undefined").getId());
        }
    }
}
