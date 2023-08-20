package com.grahql.example.datasource.component.fake;

import com.grahql.example.datasource.faker.FakerPetDataSource;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.Cat;
import com.graphql.example.types.Dog;
import com.graphql.example.types.Pet;
import com.graphql.example.types.PetFilter;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class FakerPetDataResolver {
    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.Pets)
    public List<Pet> getPets(@InputArgument(name = "petFilter") Optional<PetFilter> filter) {
        if (filter.isEmpty()) {
            return FakerPetDataSource.PET_LIST;
        }
        return FakerPetDataSource.PET_LIST.stream().filter( pet -> this.matchFilter(filter.get(), pet))
                .collect(Collectors.toList());
    }

    private boolean matchFilter(PetFilter filter, Pet pet) {
        if (StringUtils.isBlank(filter.getPetType())) {
            return true;
        }

        if (filter.getPetType().equalsIgnoreCase(Dog.class.getSimpleName())) {
            return pet instanceof Dog;
        } else if (filter.getPetType().equalsIgnoreCase(Cat.class.getSimpleName())) {
            return pet instanceof Cat;
        } else {
            return false;
        }
    }
}
