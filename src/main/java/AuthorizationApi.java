import retrofit2.Call;
import retrofit2.http.*;

public interface AuthorizationApi {
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=UTF-8")
    @POST("identityserver/connect/token")
    Call<AccessToken> getAccessToken(@Field("scope") String scope, @Field("grant_type") String grantType, @Field("client_assertion_type") String clientAssertionType, @Field("client_assertion") String clientAssertion);
}
