package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.fasterxml.jackson.databind.util.StdConverter;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListApplicationSerializer extends StdConverter<List<HelpModelApplication>,
        List<String>> {
    @Override
    public List<String> convert(List<HelpModelApplication> helpModelApplications) {
        return helpModelApplications.stream().map(HelpModelApplication::toApplication).collect(Collectors.toList());
    }
}
