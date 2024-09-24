package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private static Map<String, JSONObject> countriesJSON = new HashMap();
    private static String alpha3 = "alpha3";
    private List<String> countriesList = new ArrayList<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                countriesJSON.put(jsonArray.getJSONObject(i).getString(alpha3), jsonArray.getJSONObject(i));
                countriesList.add(jsonArray.getJSONObject(i).getString(alpha3));
            }
            System.out.println(jsonArray.length());
            System.out.println(countriesList.size());

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        JSONObject countryLang = countriesJSON.get(country.toLowerCase());
        ArrayList<String> languages = new ArrayList<>();
        for (Iterator<String> it = countryLang.keys(); it.hasNext();) {
            String key = it.next();
            if (!("id".equals(key) || "alpha2".equals(key) || "alpha3".equals(key))) {
                languages.add(key);
            }
        }
        return languages;
    }

    @Override
    public List<String> getCountries() {
        return countriesList;
    }

    @Override
    public String translate(String country, String language) {
        List<String> countryLanguages = getCountryLanguages(country);
        if (!countryLanguages.contains(language)) {
            return null;
        }
        return countriesJSON.get(country.toLowerCase()).getString(language);
    }
}
