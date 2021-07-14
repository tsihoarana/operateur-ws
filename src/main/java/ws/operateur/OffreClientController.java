package ws.operateur;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ws.model.*;
import ws.services.*;
import ws.template_json.*;
import ws.tools.Tools;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;



@RestController
public class OffreClientController {
    @PostMapping("/offre/client/achat")
    public CustomResponse<?> insert_offre_client (
        @RequestHeader Map<String, String> headers,
        @RequestBody OffreClientJSON input
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            con.setAutoCommit(false);

            // check token client
            ClientService.tokenValidateOrThrows(con, headers);

            Offre offre = OffreService.getOffreById(con, input.getIdoffre());
            input.validateOrThrows();
            // verifier credit
            StatusClient status = ClientService.getStatusClientComplet(con, input.getIdclient());
            if (status.getTotal_mvola().doubleValue() < offre.getPrix().doubleValue()) {
            	throw new Exception("Mvola insuffisant");
            }

            // ajoute la transaction
            OffreClientService.ajouter(con, offre, input);
            MvolaJSON inputvola = new MvolaJSON();
            inputvola.setIdclient(input.getIdclient());
            inputvola.setMontant(offre.getPrix().doubleValue());
            MvolaService.operer(con, inputvola, "retrait", CustomConfig.IGNORE_VALIDATION);

            con.commit();
            return CustomResponse.success("success: achat offre");
        } catch (Exception ex) {
            PGService.rollback(con);
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @GetMapping("/offre/client/{idclient}")
    public CustomResponse<?> offre_valide (
        @PathVariable(required = true) String idclient,
        @RequestHeader Map<String, String> headers,
       @RequestParam(value = "date", defaultValue = "") String date
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            con.setAutoCommit(false);
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            
            ArrayList<OffreActif> all = OffreClientService.getOffreActif(con, idclient);
            return CustomResponse.success(all);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }
}
