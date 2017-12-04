package com.foodfinder.food.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CompositionDeserializer extends StdDeserializer<CompositionDTO> {

    private static final String BAD_RESPONSE = "--";

    public CompositionDeserializer() {
        this(null);
    }

    public CompositionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CompositionDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Long nutrient_id = (Long) node.get("nutrient_id").numberValue();
        String nutrient = node.get("nutrient").asText();
        String unit = node.get("unit").asText();
        String textValue = node.get("value").asText();
        String textGm = node.get("gm").asText();
        Float value = textValue.equals(BAD_RESPONSE) ? -1f : Float.valueOf(textValue);
        Float gm = textGm.equals(BAD_RESPONSE) ? -1f : Float.valueOf(textGm);

        return CompositionDTO.builder()
                .nutrient_id(nutrient_id)
                .nutrient(nutrient)
                .unit(unit)
                .value(value)
                .gm(gm)
                .build();
    }
}
