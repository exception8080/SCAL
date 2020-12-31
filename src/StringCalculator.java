import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

	public static final String FAILED_MISSMATCHING_DELIMITERS = "Failed: Missmatching delimiters passed OR numbers part passed at same line as the custom delimiter's declartion line OR invalid input";

	String escapeRegExReservered(String text) {
		return text.replaceAll("[\\<\\(\\[\\{\\\\\\^\\-\\=\\$\\!\\|\\]\\}\\)\\?\\*\\+\\.\\>]", "\\\\$0");
	}

	String extractNegitives(String text) {
		String negitiveNumbers = text.replaceAll("(?:^|(?<![-0-9]))([0-9]+,?)", "");
		return negitiveNumbers.endsWith(",") ? negitiveNumbers.substring(0, negitiveNumbers.length() - 1)
				: negitiveNumbers;
	}

	int add(String numbers) {
		String customDelimiterPattern = "^//.+\\R", numberPart = numbers.replaceAll(",(\\R)?|(\\R)?,", ",");
		if (numberPart.matches(customDelimiterPattern.concat(".+$"))) {
			Pattern pattern = Pattern.compile(customDelimiterPattern);
			Matcher matcher = pattern.matcher(numberPart);
			if (matcher.find()) {
				String customDelimiter = matcher.group().substring(2).trim();
				// TODO: Note: In multiple delimiter, “//$,@\\n1$2@3”, it's abit unclear whether
				// the multiple delimiters are consumed in sequence they appear in delimiter
				// part or it is the case of matching ANY of the delimiter in numbers part
				// I assumed ANY
				boolean mixedDelimiter = customDelimiter.length() > 1 && customDelimiter.contains(",");
				customDelimiter = escapeRegExReservered(customDelimiter);
				if (mixedDelimiter) {
					// handles both arbitrary length + multiple delimiters
					customDelimiter = customDelimiter.replaceAll(",", "|");
				}
				numberPart = numberPart.substring(matcher.end()).replaceAll(customDelimiter, ",");
			}
		}
		if (numberPart.trim().length() == 0) {
			return 0;
		}
		if (numberPart.contains("-")) {
			throw new IllegalArgumentException("Negatives not allowed: Offending list " + extractNegitives(numberPart));
		}
		if (!numberPart.matches("(\\d+)(,\\d+)*")) {
			throw new IllegalArgumentException(FAILED_MISSMATCHING_DELIMITERS);
		}
		int sum = Arrays.stream(numberPart.split(","))
				.mapToInt(num -> Integer.parseInt(num) > 1000 ? 0 : Integer.parseInt(num)).sum();
		return sum;

	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		//@formatter:off
		System.out.println("-----------------------------------------------------\n"
				+ "\t\t String Calculator \n"
				+ "-----------------------------------------------------\n" 
				+ " See the Examples below to input a string to add numbers\n\n"
				+ " Example: “1,2,5” - Result: 8\n" 
				+ " Example: “1\\n,2,3” - Result: 6\n" 
				+ " Example: “1,\\n2,4” - Result: 7\n" 
				+ " Example: “//;\\n1;3;4” - Result: 8\n" 
				+ " Example: “//$\\n1$2$3” - Result: 6\n" 
				+ " Example: “//@\\n2@3@8” - Result: 13\n" 
				+ " Example:  “2,1001” - Result: 2\n" 
				+ " Example: “//***\\n1***2***3” - Result 6\n" 
				+ " Example: “//$,@\\n1$2@3” - Result 6\n" 
				+ " Example: “//$$$,@\\n1$$$2@3” - Result 6\n");				
		//@formatter:on
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			String input = "";
			boolean hasMoreInput = true;
			System.out.println("Enter the String below to add; Waiting...........!");
			System.out.println("(Note: Hit Enter twice to end the input String and get the sum)");

			while (hasMoreInput) {
				String nextLineString = scanner.nextLine();
				input = input.concat(nextLineString + "\n");
				hasMoreInput = !nextLineString.equals("");
			}
			System.out.println("Sum = " + new StringCalculator().add(input.trim()));
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
}
