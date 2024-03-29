package com.exasol.adapter.commontests.scalarfunction;

import java.io.*;
import java.util.*;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import com.exasol.errorreporting.ExaError;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * This class caches the parameters for runs that were passed the tests. By that in upcoming test runs can only test
 * these runs and by that are a lot faster.
 */
public class ScalarFunctionsParameterCache {
    private static final String CACHE_FILE_NAME = "src/test/resources/integration/scalarFunctionsParameterCache.yml";
    private final Map<String, List<String>> parameterCache;
    private final Yaml yaml;

    /**
     * Create a new cache.
     */
    public ScalarFunctionsParameterCache() {
        try {
            this.yaml = constructYamlSafely();
            this.parameterCache = loadParameterCache();
        } catch (final FileNotFoundException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSSIT-3")
                    .message("Failed to read scalar functions from parameters cache.").toString(), exception);
        }
    }

    /**
     * This method uses SnakeYaml's safe constructor to create a new instance.
     * <p>
     * This is done to avoid potential deserialization of external content.
     * See <a href="https://nvd.nist.gov/vuln/detail/CVE-2022-1471">CVE-2022-1471</a> for details.
     * </p>
     *
     * @return new {@link Yaml} instance
     */
    private static Yaml constructYamlSafely() {
        final LoaderOptions options = new LoaderOptions();
        return new Yaml(new SafeConstructor(options));
    }

    private Map<String, List<String>> loadParameterCache() throws FileNotFoundException {
        final Map<String, List<String>> loadedData = this.yaml.load(new FileReader(CACHE_FILE_NAME));
        if (loadedData != null) {
            return new TreeMap<>(loadedData);
        } else {
            return new TreeMap<>();
        }
    }

    /**
     * Check if the cache contains parameters for a given function.
     * 
     * @param functionName the function to check
     * @return {@code true} if the cache contains parameters for the given function.
     */
    public synchronized boolean hasParametersForFunction(final String functionName) {
        return this.parameterCache.containsKey(functionName) && !this.parameterCache.get(functionName).isEmpty();
    }

    /**
     * Get the cached parameters for the given function.
     * 
     * @param functionName the function
     * @return list of valid parameter combinations
     */
    public synchronized List<String> getFunctionsValidParameterCombinations(final String functionName) {
        return this.parameterCache.get(functionName);
    }

    /**
     * Insert the given parameters into the cache.
     * 
     * @param functionName                the function to insert
     * @param validParametersCombinations the parameters to insert
     */
    public synchronized void setFunctionsValidParameterCombinations(final String functionName,
            final List<String> validParametersCombinations) {
        this.parameterCache.put(functionName, validParametersCombinations);
    }

    /**
     * Remove the given function from the cache.
     * 
     * @param functionName the function to remove
     */
    public synchronized void removeFunction(final String functionName) {
        this.parameterCache.remove(functionName);
    }

    /**
     * Write the cache to disk.
     */
    public synchronized void flush() {
        try (final FileWriter fileWriter = new FileWriter(CACHE_FILE_NAME)) {
            this.yaml.dump(this.parameterCache, fileWriter);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSSIT-4")
                            .message("Failed to write scalar functions parameter to the cache file.").toString(),
                    exception);
        }
    }
}
