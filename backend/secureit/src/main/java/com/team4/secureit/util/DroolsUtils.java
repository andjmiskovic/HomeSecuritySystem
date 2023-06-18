package com.team4.secureit.util;

import com.team4.secureit.model.Device;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DroolsUtils {

    public static final String TEMPLATE_DIR = "templates";

    public enum Parsing {

        AS_INT("Integer.parseInt($val)"),
        AS_LONG("Long.parseLong($val)"),
        AS_FLOAT("Float.parseFloat($val)"),
        AS_STRING("$val"),
        AS_DOUBLE("Double.parseDouble($val)"),
        AS_BOOLEAN("Boolean.parseBoolean($val)");

        private final String expression;

        Parsing(String expression) {
            this.expression = expression;
        }

        public String getExpression() {
            return this.expression;
        }
    }


    private static final ConcurrentMap<UUID, KieSession> deviceKieSessions = new ConcurrentHashMap<>();

    public static String renderDRL(String templateName, DataProvider dataProvider) {
        String templatePath = Paths.get(TEMPLATE_DIR, templateName).toString();
        InputStream template = DroolsUtils.class.getClassLoader().getResourceAsStream(templatePath);
        return new DataProviderCompiler().compile(
                dataProvider,
                template
        );
    }

    public static KieSession createKieSessionFromDRL(String drl){
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);
        return kieHelper.build().newKieSession();
    }

    public static KieSession getKieSession(Device device) {
        return deviceKieSessions.get(device.getId());
    }

    public static KieSession getKieSession(UUID deviceId) {
        return deviceKieSessions.get(deviceId);
    }

    public static KieSession setKieSession(Device device, KieSession kieSession) {
        return deviceKieSessions.put(device.getId(), kieSession);
    }

    public static KieSession setKieSession(UUID deviceId, KieSession kieSession) {
        return deviceKieSessions.put(deviceId, kieSession);
    }
}
