package authenticationSystem_authServer.authServer.bcrypt;

public interface Bcrypt {
    String encrypt(String rawPassword);
    Boolean matching(String rawPassword, String decodingPassword);
}
