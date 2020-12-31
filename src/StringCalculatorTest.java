import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class StringCalculatorTest {
	StringCalculator stringCalculator;

	@Before
	public void setUp() throws Exception {
		stringCalculator = new StringCalculator();
	}

	/**
	 * 1. Testing simple addition when:<br>
	 * 
	 * a. The numbers in the string are separated by a comma.<br>
	 * b. Empty strings should return 0. <br>
	 * c. The return type should be an integer.<br>
	 * d. Example input: “1,2,5” - expected result: “8”.<br>
	 */
	@Test
	public void addNumbersCommaSeparatedTest() {
		assertEquals(stringCalculator.add(""), 0);
		assertEquals(stringCalculator.add("1,2,5"), 8);
	}

	/**
	 * 2. Test the Add method to handle new lines in the input format<br>
	 * <br>
	 * a. Example: “1\n,2,3” - Result: “6”<br>
	 * b. Example 2: “1,\n2,4” - Result: “7”<br>
	 */
	@Test
	public void addNumbersCommaSeparatedWithNewLinesTest() {
		assertEquals(stringCalculator.add("1\n,2,3"), 6);
		assertEquals(stringCalculator.add("1,\n2,4"), 7);
		assertEquals(stringCalculator.add("1,\n2,\n4"), 7);
	}

	/**
	 * 3. Test add numbers separated with a custom delimiter<br>
	 * <br>
	 * a. The beginning of your string will now contain a small control code that
	 * lets you set a custom delimiter.<br>
	 * b. Format: “//[delimiter]\n[delimiter separated numbers]” <br>
	 * c. Example: “//;\n1;3;4” - Result: 8 <br>
	 * d. In the above you can see that following the double forward slash we set a
	 * semicolon, followed by a new line. We then use that delimiter to split our
	 * numbers.<br>
	 * e. Other examples<br>
	 * i. “//$\n1$2$3” - Result: 6 <br>
	 * ii. “//@\n2@3@8” - Result: 13
	 */

	@Test
	public void addNumbersWithCustomDelimeterLinesTest() {
		assertEquals(stringCalculator.add("//;\n1;3;4"), 8);
		assertEquals(stringCalculator.add("//$\n1$2$3"), 6);
		assertEquals(stringCalculator.add("//@\n2@3@8"), 13);
		assertEquals(stringCalculator.add("//-\n2-3-8"), 13);
	}

	/**
	 * 4. Case 1. with default delimiters[,] <br>
	 * <br>
	 * Calling add with a negative number should throw an exception: “Negatives not
	 * allowed”.<br>
	 * The exception should list the number(s) that caused the exception <br>
	 * <br>
	 */

	@Test(expected = IllegalArgumentException.class)
	public void addNumbersNegativesNotAllowedDefaultDelimiterTest() {
		String errorMessagePrefix = "Negatives not allowed: Offending list ";
		try {
			stringCalculator.add("1,-3,4");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-3"));
		}
		try {
			stringCalculator.add("-1,-3,-5,4");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-1,-3,-5"));
		}
		try {
			stringCalculator.add("1,-3,-5,-44");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-3,-5,-44"));
		}
		try {
			stringCalculator.add("-1,-3,-5,-44");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-1,-3,-5,-44"));
		}
		try {
			stringCalculator.add("-1,3,5,-44");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-1,-44"));
			throw e;
		}
	}

	/**
	 * 4. Case 2. with default custom delimiters “//[delimiter]\n[delimiter
	 * separated numbers]” <br>
	 * <br>
	 * Calling add with a negative number should throw an exception: “Negatives not
	 * allowed”.<br>
	 * The exception should list the number(s) that caused the exception <br>
	 * <br>
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void addNumbersNegativesNotAllowedCustomDelimiterTest() {
		String errorMessagePrefix = "Negatives not allowed: Offending list ";
		try {
			stringCalculator.add("//....\n6....-5....3");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-5"));
		}
		try {
			stringCalculator.add("//....\n-1....-3....-5....4");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-1,-3,-5"));
		}
		try {
			stringCalculator.add("//....\n1....-3....-5....-44");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-3,-5,-44"));
		}
		try {
			stringCalculator.add("//@@\n-1@@-3@@-5@@-44");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-1,-3,-5,-44"));
		}
		try {
			stringCalculator.add("//....\n-1....3....5....-44");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(errorMessagePrefix + "-1,-44"));
			throw e;
		}
	}

	/**
	 * Bonus 1. <br>
	 * <br>
	 * Numbers larger than 1000 should be ignored.<br>
	 * a. Example “2,1001” - Result: 2
	 */
	@Test
	public void addNumbersIgnoreGT1000Test() {
		assertEquals(stringCalculator.add("2,1001"), 2);
		assertEquals(stringCalculator.add("//....\n1....2000....3"), 4);
		assertEquals(stringCalculator.add("2,1000"), 1002);
	}

	/**
	 * Bonus 2. <br>
	 * <br>
	 * Delimiters can be arbitrary length<br>
	 * a. “//***\n1***2***3” - Result 6
	 */
	@Test
	public void addNumbersArbitraryLengthDelimitersTest() {
		assertEquals(stringCalculator.add("//***\n1***2***3"), 6);
		assertEquals(stringCalculator.add("//....\n6....5....3"), 14);
		assertEquals(stringCalculator.add("//--\n6--5--3"), 14);
	}

	/**
	 * Bonus 3. <br>
	 * <br>
	 * Allow for multiple delimiters a. “//$,@\n1$2@3” - Result 6
	 * 
	 * TODO: REVIEW: it's unclear whether the multiple/mixed delimiter are consumed
	 * in sequence the are entered in delimiter part or is the case of matching ANY
	 * of the delimiter when adding the numbers part
	 */
	@Test
	public void addNumbersMixedDelimitersTest() {
		assertEquals(stringCalculator.add("//$,@\n1$2@3"), 6);
	}

	/**
	 * Bonus 4. <br>
	 * <br>
	 * Allow for multiple delimiters a. “//$,@\n1$2@3” - Result 6<br>
	 * 
	 * Combine 2 and 3 bonus questions. Allow multiple delimiters of arbitrary
	 * length<br>
	 */
	@Test
	public void addNumbersMixedDelimitersArbitraryLengthTest() {
		assertEquals(stringCalculator.add("//$$$,@\n1$$$2@3"), 6);
		try {
			stringCalculator.add("//$$$,@\n1$2@3");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(StringCalculator.FAILED_MISSMATCHING_DELIMITERS));
		}

	}

	@Test(expected = IllegalArgumentException.class)
	public void addNumbersMissMatchedDelimiterTest() {
		try {
			stringCalculator.add("");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(StringCalculator.FAILED_MISSMATCHING_DELIMITERS));
		}
		try {
			stringCalculator.add("//@\n");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(StringCalculator.FAILED_MISSMATCHING_DELIMITERS));
		}
		try {
			stringCalculator.add("1\n,,2,\n4");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(StringCalculator.FAILED_MISSMATCHING_DELIMITERS));
		}
		assertEquals(stringCalculator.add("1\n,,2,\n4"), 7);
		try {
			stringCalculator.add("//$$$,@\n1$2@3");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getLocalizedMessage().equals(StringCalculator.FAILED_MISSMATCHING_DELIMITERS));
			throw e;
		}
	}

}
