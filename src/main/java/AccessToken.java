import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("access_token")
    private String token;
    @SerializedName("expires_in")
    private int expiresIn;
    private String type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
