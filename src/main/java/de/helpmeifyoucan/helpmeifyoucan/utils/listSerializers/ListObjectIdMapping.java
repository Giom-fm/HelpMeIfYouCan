package de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class ListObjectIdMapping extends StdConverter<List<ObjectId>, List<String>> {
    @Override
    public List<String> convert(List<ObjectId> users) {
        return users.stream().map(ObjectId::toString).collect(Collectors.toList());
    }
}
