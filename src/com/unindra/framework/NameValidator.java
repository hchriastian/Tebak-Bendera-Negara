package com.unindra.framework;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles name validation of player name for leaderboard purposes
 * 
 * 
 * 
 */
public class NameValidator {

	private String name;
	private final static String REGEX = "[A-Za-z](\\w|_|.|-){3,15}";
	private boolean isValid;

	/**
	 * Constructor
	 * @param name input of user. What is going to be validated
	 */
	public NameValidator(String name) {
		this.name = name.replaceAll("\\s", "");
		validate();
	}

	/**
	 * validate name with regex (restriction)
	 */
	private void validate() {
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(name);
		if (matcher.matches()) {
			isValid = true;
		} else {
			isValid = false;
		}
	}

	/**
	 * 
	 * @return true if is name valid, otherwise false
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * 
	 * @return validated name
	 */
	public String getValidatedName() {
		return name;
	}
}
