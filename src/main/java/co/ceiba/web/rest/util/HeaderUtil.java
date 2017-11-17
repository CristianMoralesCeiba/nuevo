package co.ceiba.web.rest.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-nuevoApp-alert", message);
        headers.add("X-nuevoApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("Un nuevo " + entityName + " fue ingresado al parqueadero." + param, param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("A " + entityName + " is updated with identifier " + param, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String id) {
        return createAlert("A " + entityName + " is deleted with identifier " + id, id);
    }
    
    public static HttpHeaders createEntityValueDeletionAlert(String mensaje, BigDecimal valor) {
        return createAlert(mensaje + NumberFormat.getCurrencyInstance().format(valor) + " COP", "valor");
    }
    
    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-nuevoApp-error", defaultMessage);
        headers.add("X-nuevoApp-params", entityName);
        return headers;
    }
}
