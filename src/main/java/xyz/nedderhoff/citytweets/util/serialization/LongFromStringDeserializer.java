package xyz.nedderhoff.citytweets.util.serialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;


public class LongFromStringDeserializer extends FromStringDeserializer<Long> {

    public LongFromStringDeserializer() {
        super(Long.class);
    }

    @Override
    protected Long _deserialize(String s, DeserializationContext deserializationContext) {
        return Long.valueOf(s);
    }
}
