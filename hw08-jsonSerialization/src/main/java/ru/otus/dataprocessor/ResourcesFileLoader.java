package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (var inputStreamReader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName)))) {
            Reader reader = new BufferedReader(inputStreamReader);
            Gson gson = new Gson();
            Measurement[] measures = gson.fromJson(reader, Measurement[].class);
            System.out.printf("Can't serialize objects from file %s%n", fileName);
            return List.of(measures);
        } catch (IOException e) {
            System.out.printf("Can't serialize objects from file %s%n", fileName);
            throw new FileProcessException(e.getMessage());
        }
    }
}
