package ws.operateur;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ws.model.CustomConfig;
import ws.model.CustomResponse;
import ws.model.MvolaOrCreditTotal;
import ws.model.MvolaWithNumero;
import ws.services.ClientService;
import ws.services.MvolaService;
import ws.services.PGService;
import ws.template_json.MvolaJSON;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@RestController
public class MvolaController {

    @GetMapping(value = {"/mvola/total", "/mvola/total/{numero}"})
    public CustomResponse<?> mvola_total (
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
                ArrayList<MvolaOrCreditTotal> all = MvolaService.getAllMvolaTotal(con);
                return CustomResponse.success(all);
            }
            return CustomResponse.success(MvolaService.getMvolaTotalFor(con, numero));
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @GetMapping(value = {"/mvola/{filtre}", "/mvola/{filtre}/{numero}"})
    public CustomResponse<?> mvola_data (
        @PathVariable(required = true) String filtre, // all, valide, invalide
        @PathVariable(required = false) String numero,
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            ArrayList<MvolaWithNumero> all = MvolaService.getAllMvolaRowsFor(con, numero, filtre);

            return CustomResponse.success(all);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @PostMapping("/mvola/action/{action}")
    public CustomResponse<?> mvola_depot_retrait (
        @PathVariable(required = true) String action, // soit depot soit retrait
        @RequestHeader Map<String, String> headers,
        @RequestBody MvolaJSON input
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            // ajoute la transaction
            MvolaService.operer(con, input, action, CustomConfig.WITH_VALIDATION);
            return CustomResponse.success("succes " + action);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @PutMapping("/mvola/valider/{idmvola}")
    public CustomResponse<?> mvola_valider (
        @PathVariable(required = true) String idmvola,
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            // ajoute la transaction
            MvolaService.valider(con, idmvola);
            return CustomResponse.success("succes validation " + idmvola);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @PutMapping("/mvola/annuler/{idmvola}")
    public CustomResponse<?> mvola_annuler (
        @PathVariable(required = true) String idmvola,
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            // ajoute la transaction
            MvolaService.annuler(con, idmvola);
            return CustomResponse.success("succes annulation " + idmvola);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
	}
}