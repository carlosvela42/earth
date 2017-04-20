package co.jp.nej.earth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EStringUtil extends StringUtils {

    private static final Logger LOG = LoggerFactory.getLogger(EStringUtil.class);

    public static final String EMPTY = "";

    public static boolean checkAlphabet(String string) {
        for (char c : string.toCharArray()) {
            if (!(Character.isLetter(c)) && !(Character.isDigit(c))){
                return false;
            }
        }
        return true;
    }

    public static boolean contains(String str1, String str2) {
        if (isEmpty(str1) || isEmpty(str2)) {
            return false;
        }

        if (str1.contains(str2)) {
            return true;
        }

        return false;
    }

    public static List<String> getListString(String str,String stringIndex) {
        List<String> strings = new ArrayList<String>();
        try {
            strings = Arrays.asList(str.split(stringIndex));
            return strings;

        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error(" Convert string to list string : "+ex.getMessage());
            return strings;
        }
    }
}