package co.jp.nej.earth.model.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;

/**
 * 
 * @author p-tvo-sonta
 *
 */
public class MapAdapter extends XmlAdapter<MapWrapper, Map<String, Object>> {

    @SuppressWarnings("unchecked")
    @Override
    public MapWrapper marshal(Map<String, Object> m) throws Exception {
        MapWrapper wrapper = new MapWrapper();
        List<Object> elements = new ArrayList<>();
        for (Map.Entry<String, Object> property : m.entrySet()) {

            if (property.getValue() instanceof Map)
                elements.add(new JAXBElement<MapWrapper>(new QName(getCleanLabel(property.getKey())), MapWrapper.class,
                        marshal((Map<String, Object>) property.getValue())));
            else
                elements.add(new JAXBElement<String>(new QName(getCleanLabel(property.getKey())), String.class,
                        property.getValue().toString()));
        }
        wrapper.elements = elements;
        return wrapper;
    }

    @Override
    public Map<String, Object> unmarshal(MapWrapper v) throws Exception {
        Map<String, Object> result = new HashMap<>();
        for (Object ob : v.elements) {
            Element e = (Element) ob;
            result.put(e.getNodeName(), e.getFirstChild().getNodeValue());
        }
        return result;
    }

    // Return a XML-safe attribute. Might want to add camel case support
    private String getCleanLabel(String attributeLabel) {
        attributeLabel = attributeLabel.replaceAll("[()]", "").replaceAll("[^\\w\\s]", "_").replaceAll(" ", "_");
        return attributeLabel;
    }
}

class MapWrapper {
    @XmlAnyElement
    List<Object> elements;
}
