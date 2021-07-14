package ws.operateur;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ws.model.CustomResponse;
import ws.model.MvolaOrCreditTotal;
import ws.model.CreditWithNumero;
import ws.services.ClientService;
import ws.services.CreditService;
import ws.services.PGService;
import ws.template_json.CreditJSON;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@RestController
public class CreditController {

    @GetMapping(value = {"/credit/total", "/credit/total/{numero}"})
    public CustomResponse<?> credit_total (
        @PathVariable(required = false) String numero, // facultative
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            if (numero == null) {
                // fetch all
                ArrayList<MvolaOrCreditTotal> all = CreditService.getAllCreditTotal(con);
                return CustomResponse.success(all);
            }
            return CustomResponse.success(CreditService.getCreditTotalFor(con, numero));
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @GetMapping("/credit/all/{numero}")
    public CustomResponse<?> credit_data (
        @PathVariable(required = true) String numero,
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            ArrayList<CreditWithNumero> all = CreditService.getAllCreditRowsFor(con, numero);

            return CustomResponse.success(all);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @PostMapping("/credit/action/{action}")
    public CustomResponse<?> mvola_depot_retrait (
        @PathVariable(required = true) String action, // soit depot soit retrait
        @RequestHeader Map<String, String> headers,
        @RequestBody CreditJSON input
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            con.setAutoCommit(false);
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            // ajoute la transaction
            CreditService.operer(con, input, action);

            con.commit();
            return CustomResponse.success("succes " + action);
        } catch (Exception ex) {
            PGService.rollback(con);
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }
}