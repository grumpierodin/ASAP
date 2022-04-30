package com.grumpierodin.asap.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.grumpierodin.asap.repository.data.Rules;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class AsapFileUtils {
    public void copyCurrent() throws IOException {
        if(new File("rules.json").exists()) {
            String stamp = String.valueOf(new Date().toInstant().getEpochSecond());
            FileUtils.copyFile(new File("rules.json"), new File("rules.json" + stamp), true);
        }
    }

    public void saveCurrent(Object data) throws IOException {
        copyCurrent();
        Rules rules = (Rules)data;
        JsonUtils.WriteToFile(new File("rules.json"), rules.getRulesAsList());
    }
    public Rules readRulesFromFile() throws IOException {
        if(new File("rules.json").exists()) {
            return new Rules(FileUtils.readFileToString(new File("rules.json"), StandardCharsets.UTF_8));
        }
        return new Rules();
    }
}
