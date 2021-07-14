package ws.operateur;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

import com.mongodb.client.MongoClient;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ws.model.Appel;
import ws.model.Client;
import ws.model.CustomResponse;
import ws.model.OffreClient;
import ws.model.OffreClientDetails;
import ws.model.StatusClient;
import ws.services.AppelService;
import ws.services.ClientService;
import ws.services.CreditService;
import ws.services.MongoService;
import ws.services.OffreClientService;
import ws.services.PGService;
import ws.services.SimulerService;
import ws.template_json.AppelJSON;
import ws.template_json.CreditJSON;
import ws.tools.Tools;

@RestController
public class AppelController {


    @GetMapping("/appel/stats/{numero}")
    public CustomResponse<?> stat_appel (
        @PathVariable(required = true) String numero,
        @RequestHeader Map<String, String> headers
	) {
        Connection con = null;
		MongoClient client = null;
		try {
            con = PGService.getConnection();
            // verification numero
            ClientService.getClientByNumero(con, numero);
            // verifier token client
            ClientService.tokenValidateOrThrows(con, headers);

            client = MongoService.getClient();
            return CustomResponse.success(AppelService.getDuree(client, numero));
        } catch (Exception ex) {
			return CustomResponse.error(ex.getMessage());
		} finally {
            PGService.close(con);
        }
    }
    @GetMapping("/appel/{numero}")
    public CustomResponse<?> appel_history (
        @PathVariable(required = true) String numero,
        @RequestHeader Map<String, String> headers
	) {
        Connection con = null;
		MongoClient client = null;
		try {
            con = PGService.getConnection();
            // verification numero
            ClientService.getClientByNumero(con, numero);
            // verifier token client
            ClientService.tokenValidateOrThrows(con, headers);

            client = MongoService.getClient();
            ArrayList<Document> docs = AppelService.getHistoryAppel(client, numero);

            return CustomResponse.success(docs);
        } catch (Exception ex) {
			return CustomResponse.error(ex.getMessage());
		} finally {
            PGService.close(con);
        }

    }
    
    @PostMapping("/appel/simuler")
	public CustomResponse<?> simuler_appel(
        @RequestHeader Map<String, String> headers,
        @RequestBody AppelJSON input
	) {
        Connection con = null;
		MongoClient client = null;
		try {
            con = PGService.getConnection();
            
            // verifier token client
            ClientService.tokenValidateOrThrows(con, headers);
            input.validateOrThrows();
            // ClientService.getClientByNumero(con, input.getNumeroCible());
            Tools.generalNumeroChecker(input.getNumeroCible());
            client = MongoService.getClient();
            double duree = SimulerService.simulerAppel(client, con, input);
            
			return CustomResponse.success("Appel " + duree + " min reussi");
		} catch (Exception ex) {
			return CustomResponse.error(ex.getMessage());
		} finally {
            PGService.close(con);
        }
	}

    

}