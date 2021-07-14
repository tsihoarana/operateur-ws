package ws.operateur;

import java.sql.Connection;
import java.util.Map;

import com.mongodb.client.MongoClient;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ws.model.CustomResponse;
import ws.services.ClientService;
import ws.services.MongoService;
import ws.services.PGService;
import ws.services.SimulerService;
import ws.template_json.InternetJSON;
import ws.template_json.SmsJSON;
import ws.tools.Tools;


@RestController
public class SimulerController {
    @PostMapping("/simuler/sms")
	public CustomResponse<?> simuler_sms(
        @RequestHeader Map<String, String> headers,
        @RequestBody SmsJSON input
	) {
        Connection con = null;
		MongoClient client = null;
		try {
            con = PGService.getConnection();
            
            // verifier token client
            ClientService.tokenValidateOrThrows(con, headers);
            input.validateOrThrows();
            ClientService.getClientByNumero(con, input.getNumeroEnvoyeur());
            Tools.generalNumeroChecker(input.getNumeroCible());

            client = MongoService.getClient();
            SimulerService.simulerSms(client, con, input);
            
			return CustomResponse.success("Envoye sms reussi");
		} catch (Exception ex) {
			return CustomResponse.error(ex.getMessage());
		} finally {
            PGService.close(con);
        }
	}

    @PostMapping("/simuler/internet")
	public CustomResponse<?> simuler_internet(
        @RequestHeader Map<String, String> headers,
        @RequestBody InternetJSON input
	) {
        Connection con = null;
		MongoClient client = null;
		try {
            con = PGService.getConnection();
            
            // verifier token client
            ClientService.tokenValidateOrThrows(con, headers);
            input.validateOrThrows();
            ClientService.getClientByNumero(con, input.getNumero());
            client = MongoService.getClient();
            double consommer = SimulerService.simulerInternet(con, input);
            
			return CustomResponse.success("Utilisation internet de " + consommer + " mo reussi");
		} catch (Exception ex) {
			return CustomResponse.error(ex.getMessage());
		} finally {
            PGService.close(con);
        }
	}

    @PostMapping("/simuler/special_mobile/{nom}")
	public CustomResponse<?> simuler_internet(
        @PathVariable(required = true) String nom,
        @RequestHeader Map<String, String> headers,
        @RequestBody InternetJSON input
	) {
        Connection con = null;
		MongoClient client = null;
		try {
            con = PGService.getConnection();
            
            // verifier token client
            ClientService.tokenValidateOrThrows(con, headers);
            input.validateOrThrows();
            ClientService.getClientByNumero(con, input.getNumero());
            client = MongoService.getClient();
            double consommer = SimulerService.simulerAutre(con, input, nom);
            
			return CustomResponse.success("Utilisation " + nom + " de " + consommer + " mo reussi");
		} catch (Exception ex) {
			return CustomResponse.error(ex.getMessage());
		} finally {
            PGService.close(con);
        }
	}
}