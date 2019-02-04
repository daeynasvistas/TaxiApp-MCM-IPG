package pt.ipg.taxiapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id; //MongoDB

    private String realm, username, email, emailVerified, token;



    public User(String realm, String username, String email, String id) {
        this.realm = realm;
        this.username = username;
        this.email = email;
        this.emailVerified = emailVerified;
        this.id = id;
     }

    @Ignore // para room utilizar o anteriro(só pode utilizar um)
    public User(String token) {
        this.token = token;
    }

    @Ignore
    public User(String id, String email, String username) {
        this.username = username;
        this.email = email;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getId() {
        return id;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


/*

Response Json da API

 "id": "0J1XDBIKrj61cal724LHySAFCRxZboabjtBPRj0k0WB5cuGEqOSh3zvfstcCCAwk",
  "ttl": 1209600,
  "created": "2019-01-22T12:24:39.983Z",
  "userId": "5c368bce8d9ff80082616d33",
  "user": {
    "realm": "ipg",
    "username": "daniel",
    "email": "daniel@ept.pt",
    "emailVerified": false,
    "id": "5c368bce8d9ff80082616d33"
  }

public class User
{
    public string realm { get; set; }
    public string username { get; set; }
    public string email { get; set; }
    public bool emailVerified { get; set; }
    public string id { get; set; }
}

public class RootObject
{
    public string id { get; set; }
    public int ttl { get; set; }
    public DateTime created { get; set; }
    public string userId { get; set; }
    public User user { get; set; }
}

*/