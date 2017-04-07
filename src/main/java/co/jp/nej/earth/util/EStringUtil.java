package co.jp.nej.earth.util;

import org.springframework.util.StringUtils;

public class EStringUtil extends StringUtils {
	public static final String EMPTY = "";
    public static boolean checkAlphabet(String string ){
        for (char c : string.toCharArray()) {
            if(!(Character.isLetter(c)) && !(Character.isDigit(c)))  return false;
            }
        return true;
    }

    public static boolean Contains(String str1, String str2){
        if(StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)) return false;
        if(str1.contains(str2)) return true;
        return false;
    }
}
