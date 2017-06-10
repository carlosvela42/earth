package co.jp.nej.earth.util;

import co.jp.nej.earth.model.enums.Type;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Helper method for difficult display logic in view layer (freemarker)
 * An instance of this class would be injected into all views
 */
@Component
public class Helper {
    private static final int length = 8;
    public Type getDisplayType(Integer currentType, List<Type> types) {
        for(Type type : types) {
            if(type.getValue() == currentType) {
                return type;
            }
        }
        return null;
    }

    public boolean present(String date) {
        return !StringUtils.isEmpty(date);
    }

    public Map<String, String> getBuildInfo() {
        Map<String, String> buildInfo = new HashMap<>();
        Properties props = new Properties();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("build.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        buildInfo.put("version", props.getProperty("build.version", ""));
        String revision = props.getProperty("build.revision", "");
        String displayRevision = revision.length() > length ? revision.substring(0, length) : revision;
        buildInfo.put("revision", displayRevision);
        buildInfo.put("date", props.getProperty("build.date", ""));
        String url = props.getProperty("build.url", "");
        buildInfo.put("url", url + revision);

        return buildInfo;
    }
}
