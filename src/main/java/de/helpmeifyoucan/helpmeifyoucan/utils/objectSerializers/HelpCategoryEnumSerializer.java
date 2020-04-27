package de.helpmeifyoucan.helpmeifyoucan.utils.objectSerializers;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class HelpCategoryEnumSerializer implements Codec<HelpCategoryEnum> {

    @Override
    public HelpCategoryEnum decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return HelpCategoryEnum.valueOf(bsonReader.readString());
    }

    @Override
    public void encode(BsonWriter bsonWriter, HelpCategoryEnum postStatusEnum, EncoderContext encoderContext) {
        bsonWriter.writeString(postStatusEnum.toString());
    }

    @Override
    public Class<HelpCategoryEnum> getEncoderClass() {
        return HelpCategoryEnum.class;
    }
}