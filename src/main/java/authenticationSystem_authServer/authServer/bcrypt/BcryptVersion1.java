package authenticationSystem_authServer.authServer.bcrypt;

import java.util.Objects;

public class BcryptVersion1 implements Bcrypt {
    private final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    @Override
    public String encrypt(String rawPassword){
        String[] rawPasswordList = rawPassword.split("");
        StringBuilder encodingBinaryPassword = new StringBuilder();
        StringBuilder encodingPassword = new StringBuilder();
        int idx;
        for (idx = 0; idx < rawPasswordList.length; idx++) {
            String binaryString = Integer.toBinaryString((int) rawPasswordList[idx].charAt(0) + idx);
            String paddingBinaryCode = String.format("%08d", Integer.parseInt(binaryString));
            encodingBinaryPassword.append(paddingBinaryCode);
        }
        for (idx = 0; idx < encodingBinaryPassword.length(); idx += 6) {
            String parsingString;
            if (idx + 6 < encodingBinaryPassword.length()) {
                parsingString = encodingBinaryPassword.substring(idx, idx + 6);
            } else {
                parsingString = encodingBinaryPassword.substring(idx);
            }
            int encodingNumber = Integer.parseInt((Integer.valueOf(parsingString, 2)).toString());
            encodingPassword.append(BASE64.charAt(encodingNumber));
        }
        return encodingPassword.toString();
    }

    @Override
    public Boolean matching(String rawPassword, String decodingPassword) {
        String encryptPassword = encrypt(rawPassword);
        return Objects.equals(encryptPassword, decodingPassword);
    }
}
