package encrypt;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Created by Richard.Ezama on 4/22/2018.
 */

public class EncryptFile {
    public static String encryptSha256(String data)
    {
        String res="";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    data.getBytes(StandardCharsets.UTF_8));
            res=bytesToHex(encodedhash);
        }
        catch (Exception er)
        {

        }
     return res;
    }
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public static String getHMC256(String originalString)
    {
        String sha256hex = Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();
        return  sha256hex;
    }

}
