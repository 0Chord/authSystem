package authenticationSystem_authServer.authServer.getApi;

import java.util.UUID;

public class CreateApiVer1 implements CreateApi{

    @Override
    public String getApi() {
        String apiCode = UUID.randomUUID().toString();
        return apiCode;
    }
}
