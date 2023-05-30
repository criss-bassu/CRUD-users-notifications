package es.uma.informatica.notificacion;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;

public class HttpUtils {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public  static URI uri(String scheme, String host, int port, String... paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host)
                .port(port);
        for (String path: paths) {
            ub = ub.path(path);
        }
        return ub.build();
    }

    public static RequestEntity<Void> get(String scheme, String host, int port, String path, String token) {
        URI uri = uri(scheme, host,port, path);

        return RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER + token)
                .build();
    }

    public static RequestEntity<Void> get(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host,port, path);

        return RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .build();
    }

    public static RequestEntity<Void> delete(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host,port, path);
        return RequestEntity.delete(uri)
                .build();
    }

    public static RequestEntity<Void> delete(String scheme, String host, int port, String path, String token) {
        URI uri = uri(scheme, host,port, path);
        return RequestEntity.delete(uri)
                .header(AUTHORIZATION, BEARER + token)
                .build();
    }

    public static <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        return RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
    }

    public static <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object, String token) {
        URI uri = uri(scheme, host,port, path);
        return RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER + token)
                .body(object);
    }

    public static <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host,port, path);
        return RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
    }

    public static <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object, String token) {
        URI uri = uri(scheme, host,port, path);
        return RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER + token)
                .body(object);
    }


    public static <T> RequestEntity<T> postConParams(String http, String localhost, int i, String s, T tipo) {
        URI uri = uri(http, localhost, i, s);
        return RequestEntity.post(uri)
                .body(tipo);
    }

    public static <T> RequestEntity.BodyBuilder postBuilder(String shceme, String host, int port, String path, String param) {
        URI uri = uri(shceme, host, port, path);
        return RequestEntity.post(uri);
    }
}
