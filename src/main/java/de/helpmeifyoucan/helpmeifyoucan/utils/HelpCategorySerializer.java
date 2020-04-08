package de.helpmeifyoucan.helpmeifyoucan.utils;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.List;
import java.util.stream.Collectors;

public class HelpCategorySerializer extends StdConverter<List<String>, List<HelpCategoryEnum>> {

    @Override
    public List<HelpCategoryEnum> convert(List<String> strings) {
        return strings.stream().map(x -> convert(x)).collect(Collectors.toList());
    }


    public HelpCategoryEnum convert(String s) {
        return HelpCategoryEnum.valueOf(s);
    }
}
