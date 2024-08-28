package bajajtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar DestinationHashGenerator.jar <240343020067> <path to JSON file>");
            System.exit(1);
        }

        String prnNumber = args[0].trim().toLowerCase();
        String jsonFilePath = args[1].trim();

        try {
            // Step 1: Read and parse JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("D:/know-it data/CDAC Project/example.json"));

            // Step 2: Traverse JSON to find the first instance of the key "destination"
            String destinationValue = findDestinationValue(rootNode);

            if (destinationValue == null) {
                System.out.println("Key 'destination' not found in the JSON file.");
                System.exit(1);
            }

            // Step 3: Generate a random alphanumeric string
            String randomString = generateRandomString(8);

            // Step 4: Concatenate PRN Number, destination value, and random string
            String concatenatedString = prnNumber + destinationValue + randomString;

            // Step 5: Generate MD5 hash
            String md5Hash = generateMD5Hash(concatenatedString);

            // Step 6: Output the result in the specified format
            System.out.println(md5Hash + ";" + randomString);

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String findDestinationValue(JsonNode node) {
        if (node.has("destination")) {
            return node.get("destination").asText();
        }
        if (node.isArray()) {
            for (JsonNode element : node) {
                String result = findDestinationValue(element);
                if (result != null) {
                    return result;
                }
            }
        } else if (node.isObject()) {
            for (JsonNode child : node) {
                String result = findDestinationValue(child);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private static String generateMD5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

