package org.tzi.use.psum.parser;

import org.tzi.use.psum.model.*;
import org.tzi.use.psum.model.enums.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic scanner-based parser for .psum sidecar files.
 * Format:
 * psum ModelName
 * 
 * agent A { type = "expert" }
 * uncertainty U : class.attribute { kind = Measurement \n nature = Aleatory }
 * belief B { agent = A \n subject = attribute class.attr }
 */
public class PSUMParser {
    
    public PSUMModel parse(File file) throws IOException {
        String content = readFile(file);
        return parse(content);
    }
    
    public PSUMModel parse(String content) {
        String[] lines = content.replace("\r", "").split("\n");
        PSUMModel model = new PSUMModel("Unknown");
        Map<String, PSUMElement> elementCache = new HashMap<>();
        
        String currentBlockType = null;
        String currentBlockId = null;
        String currentBlockTarget = null;
        Map<String, String> currentProperties = new HashMap<>();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//") || line.startsWith("#")) {
                continue;
            }
            
            if (line.startsWith("psum ")) {
                model = new PSUMModel(line.substring(5).trim());
            } else if (line.endsWith("{") || line.contains("{")) {
                // start of block e.g., "agent sensor1 {" or "uncertainty u1 : Room.temp {"
                String declaration = line.substring(0, line.indexOf('{')).trim();
                String[] parts = declaration.split("\\s+", 3);
                currentBlockType = parts[0];
                currentBlockId = parts[1];
                if (declaration.contains(":")) {
                    currentBlockTarget = declaration.substring(declaration.indexOf(':') + 1).trim();
                } else {
                    currentBlockTarget = null;
                }
                currentProperties.clear();
            } else if (line.startsWith("}")) {
                // block ends, build object
                PSUMElement element = buildElement(currentBlockType, currentBlockId, currentBlockTarget, currentProperties, elementCache);
                if (element != null) {
                    model.addElement(element);
                    elementCache.put(element.getId(), element);
                }
                currentBlockType = null;
            } else if (currentBlockType != null && line.contains("=")) {
                // properties
                String[] parts = line.split("=", 2);
                currentProperties.put(parts[0].trim(), parts[1].replace("\"", "").trim());
            }
        }
        
        return model;
    }
    
    private PSUMElement buildElement(String type, String id, String target, Map<String, String> props, Map<String, PSUMElement> cache) {
        if ("agent".equalsIgnoreCase(type)) {
            BeliefAgent agent = new BeliefAgent(id);
            agent.setType(props.get("type"));
            return agent;
        } else if ("uncertainty".equalsIgnoreCase(type)) {
            Uncertainty u = new Uncertainty(id);
            if (target != null) {
                UncertaintyTopic topic = new UncertaintyTopic(id + "_topic");
                topic.setTargetModelElement(target);
                u.setSubject(topic);
            }
            u.setKind(parseEnum(UncertaintyKind.class, props.get("kind")));
            u.setNature(parseEnum(UncertaintyNature.class, props.get("nature")));
            u.setReducibility(parseEnum(ReducibilityLevel.class, props.get("reducibility")));
            u.setPerspective(parseEnum(UncertaintyPerspective.class, props.get("perspective")));
            if (props.containsKey("source") && cache.containsKey(props.get("source"))) {
                PSUMElement src = cache.get(props.get("source"));
                if(src instanceof IndeterminacySource) u.setSource((IndeterminacySource) src);
            }
            return u;
        } else if ("belief".equalsIgnoreCase(type)) {
            Belief b = new Belief(id);
            if (props.containsKey("agent")) {
                PSUMElement a = cache.get(props.get("agent"));
                if (a instanceof BeliefAgent) b.setAgent((BeliefAgent) a);
            }
            if (props.containsKey("subject")) {
                UncertaintyTopic topic = new UncertaintyTopic(id + "_topic");
                topic.setTargetModelElement(props.get("subject").replace("attribute", "").trim());
                b.setSubject(topic);
            }
            b.setKind(parseEnum(UncertaintyKind.class, props.get("kind")));
            b.setNature(parseEnum(UncertaintyNature.class, props.get("nature")));
            b.setReducibility(parseEnum(ReducibilityLevel.class, props.get("reducibility")));
            b.setPerspective(parseEnum(UncertaintyPerspective.class, props.get("perspective")));
            return b;
        }
        return null;
    }
    
    private <T extends Enum<T>> T parseEnum(Class<T> enumClass, String value) {
        if (value == null) return null;
        try {
            // Support snake_case vs CamelCase matching (e.g. PartiallyReducible -> PARTIALLY_REDUCIBLE)
            String converted = value.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase();
            return Enum.valueOf(enumClass, converted);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
