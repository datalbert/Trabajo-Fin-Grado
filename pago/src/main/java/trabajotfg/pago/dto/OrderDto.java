package trabajotfg.pago.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data @AllArgsConstructor
public class OrderDto {

String email;
int id_reserva;
Double price;
String currency;
String method;
String intent;
String description;

}

//currency es la moneda
//method es paypal
//intent es sale
//description es la descripcion_metodo de pago


