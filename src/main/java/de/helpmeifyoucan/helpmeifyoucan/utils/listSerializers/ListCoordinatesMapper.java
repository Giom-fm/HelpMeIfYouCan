package de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers;

import com.fasterxml.jackson.databind.util.StdConverter;
import de.helpmeifyoucan.helpmeifyoucan.models.Coordinates;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListCoordinatesMapper extends StdConverter<List<Coordinates>, List<String>> {

    @Override
    public List<String> convert(List<Coordinates> coordinates) {
        return coordinates.stream().map(Coordinates::toJson).collect(Collectors.toList());
    }
}
