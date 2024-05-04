import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static Scanner sc;

    private static void encode() throws InterruptedException {
        EnigmaEncoder encoder = new EnigmaEncoder();
        System.out.println("---ENIGMA-ENCODER---");
        boolean prev = false;
        while(true) {
            System.out.print("1. Generate Random Key\n2. Custom Key\n3. Map Values\n4. Generate With Previous Key\n5. Back\nEnter Ur Choice: ");
            int choice = Integer.parseInt(sc.nextLine());
            if(choice == 5) return;
            if(choice == 1) {
                encoder.setRandomKey();
                System.out.println("Generated Random Key...");
            } else if(choice == 2) {
                System.out.print("Enter Custom Key: ");
                String key = sc.nextLine();
                if(key.length() != 26) {
                    System.out.println("Key Must Be Of Length 26");
                    continue;
                }
                encoder.setCustomKey(key);
                System.out.println("Custom Key Set...");
            } else if(choice == 3) {
                System.out.print("Enter Mapping Values: [Character Integer] (0 <= Integer < 26)\nEnter Number Of Mapping Values: ");
                int n = Integer.parseInt(sc.nextLine());
                char[] alpha = new char[26];
                Arrays.fill(alpha, '0');
                Set<Character> mappedChars = new HashSet<>();
                for(int i = 0; i < n; i++) {
                    String[] line = sc.nextLine().split(" ");
                    char ch = Character.toUpperCase(line[0].charAt(0));
                    int num = Integer.parseInt(line[1]);
                    if(mappedChars.contains(ch)) {
                        System.out.println("Character " + ch + " Has Already Been Mapped...");
                        i--;
                        continue;
                    }
                    if(num < 0 || num > 26 || !Character.isLetter(ch)) {
                        System.out.println("Invalid Indexing... 0 < Integer <= 26 | A,a <= Character <= Z,z");
                        i--;
                        continue;
                    }
                    alpha[num-1] = ch;
                    mappedChars.add(ch);
                }
                encoder.mapKey(alpha, mappedChars);
                System.out.println("Values Mapped...");
            } else if(choice == 4) {
                if(prev) System.out.println("Using Available Key...");
                else {
                    System.out.println("No Key Found...");
                    continue;
                }
            } else {
                System.out.println("Invalid Input... Try Again!");
                continue;
            }
            System.out.print("Enter the Message: ");
            String msg = sc.nextLine();
            System.out.println("ENCODED MSG: " + encoder.encode(msg) + "\nKEY: " + encoder.getKey());
            prev = true;
        }
    }

    private static void decode() throws InterruptedException {
        EnigmaDecoder decoder = new EnigmaDecoder();
        System.out.println("---ENIGMA-DECODER---");
        System.out.print("Enter The Key: ");
        String key = sc.nextLine();
        if(key.length() != 26) {
            System.out.println("Invalid Key...");
            return;
        }
        System.out.print("Enter The Message: ");
        String msg = sc.nextLine();
        System.out.println("DECODED MSG: " + decoder.decode(key, msg));
    }

    private static void exit() throws InterruptedException {
        System.out.println("Removing Downloaded Files...");
        Thread.sleep(1500);
        System.out.println("Removing Cache Files...");
        Thread.sleep(2000);
        System.out.println("Removing User Data...");
        Thread.sleep(1200);
        System.out.println("Exiting....");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("----ENIGMA----");
        sc = new Scanner(System.in);
        while(true) {
            System.out.print("1. Encode\n2. Decode\n3. Exit\nEnter ur choice: ");
            int choice = Integer.parseInt(sc.nextLine());
            if(choice == 1) encode();
            else if(choice == 2) decode();
            else if(choice == 3) {
                exit();
                break;
            } else System.out.println("Try Again... Invalid Input");
        }
    }
}