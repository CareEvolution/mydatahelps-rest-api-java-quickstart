import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Date;
import java.util.UUID;

public class Quickstart {

    public static void main(String[] args) {
        try {
            Quickstart app = new Quickstart();
            System.out.println("--------------------------");
            System.out.println("Obtained service access token:");
            System.out.println("  " + app.accessToken.getToken());

            ParticipantPage page = app.getParticipants("");
            System.out.println("--------------------------");
            System.out.println("Total Participants: " + page.getTotalParticipants());

/* Additional examples:
            String participantIdentifier = UUID.randomUUID().toString();
            Participant participant = new Participant();
            participant.setParticipantIdentifier(participantIdentifier);
            participant.getDemographics().setFirstName("John");
            participant.getDemographics().setLastName("Doe");
            System.out.println("--------------------------");
            System.out.println("Adding new participant");
            System.out.println("--------------------------");
            System.out.println("Participant Identifier: " + participant.getParticipantIdentifier());
            System.out.println("First Name: " + participant.getDemographics().getFirstName());
            System.out.println("Last Name: " + participant.getDemographics().getLastName());
            ParticipantPersistResult result = app.persistParticipant(participant);
            System.out.println("--------------------------");
            System.out.println("Participant Persist Result: " + result.getActionTaken());

            page = app.getParticipants("");
            System.out.println("--------------------------");
            System.out.println("Total Participant Count: " + page.getTotalParticipants());

            participant = app.getParticipant(participantIdentifier);
            System.out.println("--------------------------");
            System.out.println("Retrieving participant by identifier");
            System.out.println("--------------------------");
            System.out.println("ID: " + participant.getId());
            System.out.println("Participant Identifier: " + participant.getParticipantIdentifier());
            System.out.println("First Name: " + participant.getDemographics().getFirstName());
            System.out.println("Last Name: " + participant.getDemographics().getLastName());

            AccessToken accessToken = app.getParticipantToken(participant.getId());
            System.out.println("--------------------------");
            System.out.println("Generating participant access token");
            System.out.println("--------------------------");
            System.out.println("ID: " + participant.getId());
            System.out.println("Participant Token: " + accessToken.getToken());
            System.out.println("--------------------------");
*/            
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        System.exit(0);
    }

    private final MyDataHelpsApi myDataHelpsApi;
    private AccessToken accessToken;

    public Quickstart() {
        Security.addProvider(new BouncyCastleProvider());
        String signedAssertion = generateJwt(Config.PRIVATE_KEY, Config.SERVICE_ACCOUNT_NAME, Config.TOKEN_URL);
        accessToken = getServiceToken(Config.BASE_URL, signedAssertion);
        if (accessToken == null) {
            throw new RuntimeException("could not get service token");
        }
        myDataHelpsApi = getMyDataHelpsApi(Config.BASE_URL, accessToken.getToken());
    }

    public ParticipantPage getParticipants(String query) {
        try {
            Call<ParticipantPage> call = myDataHelpsApi.queryParticipants(Config.PROJECT_ID, query);
            Response<ParticipantPage> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            else {
                System.out.println("HTTP response code: " + response.code());
                System.out.println(response.errorBody().string());
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public Participant getParticipant(String identifier) {
        try {
            Call<Participant> call = myDataHelpsApi.getParticipant(Config.PROJECT_ID, identifier);
            Response<Participant> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            else {
                System.out.println("HTTP response code: " + response.code());
                System.out.println(response.errorBody().string());
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public ParticipantPersistResult persistParticipant(Participant participant) {
        try {
            Call<ParticipantPersistResult> call = myDataHelpsApi.persistParticipant(Config.PROJECT_ID, participant);
            Response<ParticipantPersistResult> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            else {
                System.out.println("HTTP response code: " + response.code());
                System.out.println(response.errorBody().string());
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public AccessToken getParticipantToken(String participantId) {
        try {
            Call<AccessToken> call = myDataHelpsApi.getAccessToken(
                    "api",
                    "delegated_participant",
                    "MyDataHelps-Web.DelegatedParticipant",
                    "secret",
                    participantId,
                    accessToken.getToken());
            Response<AccessToken> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            else {
                System.out.println("HTTP response code: " + response.code());
                System.out.println(response.errorBody().string());
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    private MyDataHelpsApi getMyDataHelpsApi(String baseUrl, String accessToken) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            return chain.proceed(newRequest);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(MyDataHelpsApi.class);
    }

    private AccessToken getServiceToken(String baseUrl, String signedAssertion) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();

        AuthorizationApi authApi = retrofit.create(AuthorizationApi.class);
        try {
            Call<AccessToken> call = authApi.getAccessToken(
                    "api",
                    "client_credentials",
                    "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
                    signedAssertion);
            Response<AccessToken> response = call.execute();
            if (response.isSuccessful()) {
                AccessToken accessToken = response.body();
//                System.out.println("Service token: " + accessToken.token);
                return accessToken;
            } else {
                System.out.println("HTTP response code: " + response.code());
                System.out.println(response.errorBody().string());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    private static String generateJwt(String privateKey, String serviceAccountName, String audience) {
        String token = null;
        try {
            token = Jwts.builder()
                    .setIssuer(serviceAccountName)
                    .setSubject(serviceAccountName)
                    .setAudience(audience)
                    .setExpiration(new Date(System.currentTimeMillis() + 5000))
                    .setId(UUID.randomUUID().toString())
                    .signWith(convertPrivateKey(privateKey), SignatureAlgorithm.RS256).compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    private static PrivateKey convertPrivateKey(String privateKey) {
        try {
            PEMParser pemParser = new PEMParser(new StringReader(privateKey));
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            Object object = pemParser.readObject();
            KeyPair kp = converter.getKeyPair((PEMKeyPair) object);
            PrivateKey privKey = kp.getPrivate();
//            System.out.println(privKey);
            return privKey;
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
}

