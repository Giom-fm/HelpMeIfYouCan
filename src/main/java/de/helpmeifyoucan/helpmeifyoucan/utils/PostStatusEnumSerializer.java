package de.helpmeifyoucan.helpmeifyoucan.utils;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.springframework.stereotype.Component;

@Component
public class PostStatusEnumSerializer implements Codec<PostStatusEnum> {

    @Override
    public PostStatusEnum decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return PostStatusEnum.valueOf(bsonReader.readString());
    }

    @Override
    public void encode(BsonWriter bsonWriter, PostStatusEnum postStatusEnum, EncoderContext encoderContext) {
        bsonWriter.writeString(postStatusEnum.toString());
    }

    @Override
    public Class<PostStatusEnum> getEncoderClass() {
        return PostStatusEnum.class;
    }
}
