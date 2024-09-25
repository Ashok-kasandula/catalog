import java.io.FileReader;
import java.math.BigInteger;
import java.util.Iterator;
import org.json.JSONObject;

public class catalogInt {
    public static int convertToBase10(String value, int base) {
        return Integer.parseInt(value, base);
    }

    // Method to compute the constant term (c) using the provided roots
    public static BigInteger findConstantTerm(JSONObject jsonObject) {
        BigInteger constantTerm = BigInteger.ONE;

        // Get the root information
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        if (n < k) {
            throw new IllegalArgumentException("Not enough roots provided to determine the polynomial.");
        }

        // Calculate the degree of the polynomial
        int degree = k - 1; // m = k - 1

        // Iterate through the JSON and extract root information
        Iterator<String> keysIterator = jsonObject.keys();

        while (keysIterator.hasNext()) {
            String key = keysIterator.next();

            // Ignore the "keys" object
            if (!key.equals("keys")) {
                JSONObject rootInfo = jsonObject.getJSONObject(key);
                int base = Integer.parseInt(rootInfo.getString("base"));
                String value = rootInfo.getString("value");

                // Convert the value from the given base to base 10
                int root = convertToBase10(value, base);

                // Multiply the root to get the constant term
                constantTerm = constantTerm.multiply(BigInteger.valueOf(root));
            }
        }

        // Apply the sign based on the degree of the polynomial
        if (degree % 2 != 0) { // if the degree is odd
            constantTerm = constantTerm.negate();
        }

        return constantTerm;
    }

    public static void main(String[] args) {
        try {
            // Reading the JSON file directly
            FileReader reader = new FileReader("polynomial_roots.json");

            // Parse the JSON object from the file
            StringBuilder jsonString = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                jsonString.append((char) ch);
            }
            JSONObject jsonObject = new JSONObject(jsonString.toString());

            // Calculate and display the constant term
            BigInteger constantTerm = findConstantTerm(jsonObject);
            System.out.println("The constant term of the polynomial is: " + constantTerm);

            reader.close(); // Close the FileReader

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
