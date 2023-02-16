package xyz.nedderhoff.citytweets.util.serialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

import java.io.IOException;

public class LongFromStringDeserializer extends FromStringDeserializer<Long> {

    protected LongFromStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    protected Long _deserialize(String s, DeserializationContext deserializationContext) throws IOException {
        return Long.valueOf(s);
    }
}
