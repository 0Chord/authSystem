package authenticationSystem_authServer.authServer.bcrypt;

public interface Bcrypt {
    String encrypt(String rowPassword);
    Boolean matching(String rowPassword, String decodingPassword);
}
