package helpers;
import model.Post;
import model.User;
import java.util.Random;

public class varPost {

    public static String generateRandomTitle(){
        return String.format("%s" , generateRandomString(10));
    }

    public static String generateRandomContent(){
        return String.format("%s" , generateRandomString(100));
    }

    private static String generateRandomString(int targetStringLength){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
        }

}
