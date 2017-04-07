package co.jp.nej.earth.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.constant.Constant;
import co.jp.nej.earth.service.MstSystem;
import co.jp.nej.earth.service.MstSystemService;

@Component
public class PasswordPolicy {
	@Autowired
	private MstSystemService mstSystemService;

	public List<String> validate(String password) throws EarthException, FileNotFoundException, IOException {
		List<String> listResult = null;

		List<MstSystem> mstSystems = mstSystemService.getMstSystem();

		List<Rule> listRule = ruleDefine(mstSystems);
		MessageResolver mr = messageDefine();

		PasswordValidator validator = new PasswordValidator(mr, listRule);
		RuleResult result = validator.validate(new PasswordData(new String(password)));

		if (!result.isValid()) {
			listResult = new ArrayList<String>();
			for (String msg : validator.getMessages(result)) {
				if (!msg.contains("INSUFFICIENT_CHARACTERISTICS")) {
					listResult.add(msg);
				}
			}
		}

		return listResult;
	}

	public List<Rule> ruleDefine(List<MstSystem> listRuleFromDb) {
		LengthRule ruleLength = new LengthRule();
		CharacterCharacteristicsRule rulesItem = new CharacterCharacteristicsRule();
		WhitespaceRule whitespaceRule = null;

		if (!listRuleFromDb.isEmpty()) {
			rulesItem.setNumberOfCharacteristics(listRuleFromDb.size() - 3);
			for (MstSystem item : listRuleFromDb) {
				if (Constant.RuleDefilePasswordPolicy.PASS_SHORT.equals(item.getVariableName())) {
					if (!"".equals(item.getConfigValue()) && item.getConfigValue() != null) {
						ruleLength.setMinimumLength(Integer.parseInt(item.getConfigValue()));
					}
				}

				if (Constant.RuleDefilePasswordPolicy.PASS_LONG.equals(item.getVariableName())) {
					if (!"".equals(item.getConfigValue()) && item.getConfigValue() != null) {
						ruleLength.setMaximumLength(Integer.parseInt(item.getConfigValue()));
					}
				}

				if (Constant.RuleDefilePasswordPolicy.PASS_UPPERCASE.equals(item.getVariableName())) {
					if (!"".equals(item.getConfigValue()) && item.getConfigValue() != null) {
						rulesItem.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase,
								Integer.parseInt(item.getConfigValue())));
					}
				}

				if (Constant.RuleDefilePasswordPolicy.PASS_LOWERCASE.equals(item.getVariableName())) {
					if (!"".equals(item.getConfigValue()) && item.getConfigValue() != null) {
						rulesItem.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase,
								Integer.parseInt(item.getConfigValue())));
					}
				}

				if (Constant.RuleDefilePasswordPolicy.PASS_DIGIT.equals(item.getVariableName())) {
					if (!"".equals(item.getConfigValue()) && item.getConfigValue() != null) {
						rulesItem.getRules().add(
								new CharacterRule(EnglishCharacterData.Digit, Integer.parseInt(item.getConfigValue())));
					}
				}

				if (Constant.RuleDefilePasswordPolicy.PASS_SPECIAL.equals(item.getVariableName())) {
					if (!"".equals(item.getConfigValue()) && item.getConfigValue() != null) {
						rulesItem.getRules().add(new CharacterRule(EnglishCharacterData.Special,
								Integer.parseInt(item.getConfigValue())));
					}
				}

				if (Constant.RuleDefilePasswordPolicy.PASS_WHITESPACE.equals(item.getVariableName())) {
					if (Constant.RuleDefilePasswordPolicy.WhileSpace.WhileSpace.toString()
							.equals(item.getConfigValue())) {
						whitespaceRule = new WhitespaceRule();
					}
				}
			}
		}

		if (whitespaceRule != null) {
			List<Rule> rules = Arrays.asList(ruleLength, rulesItem, whitespaceRule);
			return rules;
		} else {
			List<Rule> rules = Arrays.asList(ruleLength, rulesItem);
			return rules;
		}
	}

	public MessageResolver messageDefine() throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(getClass().getClassLoader().getResourceAsStream("paseyMessage.properties"));
		MessageResolver resolver = new PropertiesMessageResolver(props);

		return resolver;
	}

}
