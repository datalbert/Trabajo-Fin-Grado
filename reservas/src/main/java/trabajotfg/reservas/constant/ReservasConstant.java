package trabajotfg.reservas.constant;

public class ReservasConstant {

    public static final String HTTP_STATUS_OK = "200";
    public static final String HTTP_STATUS_OK_MESSAGE = "OK";

    public static final String HTTP_STATUS_BAD_REQUEST = "400";
    public static final String HTTP_STATUS_BAD_REQUEST_MESSAGE = "Bad Request";

    public static final String HTTP_STATUS_NOT_FOUND = "404";
    public static final String HTTP_STATUS_NOT_FOUND_MESSAGE = "Recurso no encontrado";

    public static final String HTTP_STATUS_CREATED = "201";
    public static final String HTTP_STATUS_CREATED_MESSAGE = "La reserva se ha realizado correctamente";

    public static final String HTTP_STATUS_INTERNAL_SERVER_ERROR = "500";
    public static final String HTTP_STATUS_INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";

    public static final String RESERVA_CANCELADA_REMBOLSO_100 = "La reserva ha sido cancelada y se ha realizado el reembolso del 100% del importe";
    public static final String RESERVA_CANCELADA_REMBOLSO_50 = "La reserva ha sido cancelada y se ha realizado el reembolso del 50% del importe";
    public static final String RESERVA_CANCELADA_ERROR = "La reserva no se puede cancelar porque ya ha pasado el tiempo límite";

}
