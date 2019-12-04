package seaBattleLogin;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class SeaBattleLogin implements ISeaBattleLogin {

    @Override
    public void registerPlayer(String name, String uPassword){
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            var request = new HttpPost("https://seabattle-s3.herokuapp.com/auth/register");
            request.setHeader("Content-type", "application/json");
            //request.setEntity(new StringEntity("{\"email\":\"julianblauw1234567891000@hotmail.com\",\"password\":\"Appeltaart16!\",\"firstName\":\"julian\",\"lastName\":\"Lu\"} "));

            Gson gson = new Gson();
            User user = new User()
            {{
                email = name;
                password = uPassword;
                lastName = "Lastname";
                firstName = "Player";
            }};

            String jsonString =  gson.toJson(user, User.class);
            request.setEntity(new StringEntity(jsonString));
            HttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String body= new String(response.getEntity().getContent().readAllBytes());

                Response resp = gson.fromJson(body, Response.class);
                String id = resp.user.id;
            } else if(response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                System.out.println("Gegevens bestaan al. U logt in");
                loginPlayer(name, uPassword);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginPlayer(String name, String uPassword) {
        try(CloseableHttpClient client = HttpClientBuilder.create().build()){
            var request = new HttpPost("https://seabattle-s3.herokuapp.com/auth/login");
            request.setHeader("Content-type","application/json");

            User user = new User()
            {{
                email = name;
                password = uPassword;
            }};

            Gson gson = new Gson();
            String jsonString = gson.toJson(user,User.class);
            request.setEntity(new StringEntity(jsonString));

            HttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String body= new String(response.getEntity().getContent().readAllBytes());

                Response resp = gson.fromJson(body, Response.class);
                String id = resp.user.id;
            } else if(response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                System.out.println("Foute gegevens");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Response {
    String token;
    User user;
}

class User {
    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
}