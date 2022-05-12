package Utils;

import io.restassured.http.ContentType;

public interface Constantes {

    String          BASE_URL = "https://barrigarest.wcaquino.me/";
    ContentType BASE_CONTENT_TYPE = ContentType.JSON;
    Long MAX_TIMEOUT         = 8000L;
}
