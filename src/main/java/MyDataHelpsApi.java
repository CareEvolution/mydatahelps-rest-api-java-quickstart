import retrofit2.Call;
import retrofit2.http.*;

public interface MyDataHelpsApi {
    @GET("api/v1/administration/projects/{projectId}/participants")
    Call<ParticipantPage> queryParticipants(@Path("projectId") String projectId, @Query("query") String query);

    @PUT("api/v1/administration/projects/{projectId}/participants")
    Call<ParticipantPersistResult> persistParticipant(@Path("projectId") String projectId, @Body() Participant participant);

    @GET("api/v1/administration/projects/{projectId}/participants/{identifier}")
    Call<Participant> getParticipant(@Path("projectId") String projectId, @Path("identifier") String identifier);

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=UTF-8")
    @POST("identityserver/connect/token")
    Call<AccessToken> getAccessToken(@Field("scope") String scope, @Field("grant_type") String grantType, @Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("participant_id") String participantId, @Field("token") String token);
}
