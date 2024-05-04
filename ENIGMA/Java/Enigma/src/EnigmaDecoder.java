import java.util.HashMap;
import java.util.Map;

public class EnigmaDecoder {

    EnigmaDecoder() throws InterruptedException {
        System.out.println("Initializing Decoder...");
        Thread.sleep(2000);
    }

    public String decode(String key, String msg) {
        Map<Character, Integer> keyMap = new HashMap<>();
        for(int i = 0; i < key.length(); i++) keyMap.put(key.charAt(i), i);
        StringBuilder decodedSb = new StringBuilder();
        for(char ch: msg.toCharArray()) {
            if(Character.isLetter(ch)) decodedSb.append((char) (keyMap.get(ch) + 65));
            else decodedSb.append(ch);
        }
        return decodedSb.toString();
    }
}
