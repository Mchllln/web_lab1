package inventory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import inventory.domain.Warehouse;

import java.io.File;
import java.io.IOException;

public class JsonStorage implements Storage {

    private final ObjectMapper objectMapper;

    public JsonStorage() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void saveToFile(Warehouse warehouse, String filename) throws IOException {
        objectMapper.writeValue(new File(filename), warehouse);
    }

    @Override
    public Warehouse loadFromFile(String filename) throws IOException {
        return objectMapper.readValue(new File(filename), Warehouse.class);
    }
}
