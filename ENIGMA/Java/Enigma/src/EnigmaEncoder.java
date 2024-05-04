import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnigmaEncoder {
    String key;

    EnigmaEncoder() throws InterruptedException {
        System.out.println("Initializing Encoder...");
        Thread.sleep(2000);
    }

    public void setRandomKey() {
        char[] alpha = new char[26];
        Arrays.fill(alpha, '0');
        for(int i = 65; i < 65+26; i++) {
            int randomIdx = (int) (Math.random() * 26);
            while(alpha[randomIdx] != '0') randomIdx = (randomIdx + 1)%26;
            alpha[randomIdx] = (char) i;
        }
        this.key = new String(alpha);
    }

    public void setCustomKey(String key) {
        this.key = key;
    }

    public void mapKey(char[] charMap, Set<Character> mappedChars) {
        for(int i = 65; i <65+26; i++) {
            if(mappedChars.contains((char) i)) continue;
            int randomIdx = (int) (Math.random() * 26);
            while(charMap[randomIdx] != '0') randomIdx = (randomIdx + 1)%26;
            charMap[randomIdx] = (char) i;
        }
        this.key = new String(charMap);
    }

    public void previousKey() {
        return;
    }

    public String getKey() {
        return key;
    }

    public String encode(String msg) {
        msg = msg.toUpperCase();
        StringBuilder encodedSb = new StringBuilder();
        char[] key = this.key.toCharArray();
        for(char ch: msg.toCharArray()){
            if(Character.isLetter(ch)) encodedSb.append(key[ch-'A']);
            else encodedSb.append(ch);
        }
        return encodedSb.toString();
    }
}
