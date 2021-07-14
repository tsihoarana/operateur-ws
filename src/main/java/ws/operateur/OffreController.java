package ws.operateur;

import java.sql.Connection;
import java.util.*;
import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ws.model.CustomConfig;
import ws.model.CustomResponse;
import ws.services.ClientService;
import ws.services.OffreService;
import ws.services.PGService;
import ws.template_json.OffreJSON;

import ws.model.Offre;
import ws.model.TypeOffre;


@RestController
public class OffreController {

    @PostMapping("/offre")
    public CustomResponse<?> insert_offre (
        @RequestHeader Map<String, String> headers,
        @RequestBody OffreJSON input
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            con.setAutoCommit(false);
            ClientService.tokenValidateOrThrows(con, headers);

            // ajoute la transaction
            input.validateOrThrows();
            OffreService.ajouterOffre(con, input); // n queries

            con.commit();
            return CustomResponse.success("success: ajout offre");
        } catch (Exception ex) {
            System.out.println(ex);
            PGService.rollback(con);
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @DeleteMapping("/offre/{idoffre}")
    public CustomResponse<?> delete_offre (
        @PathVariable(required = true) String idoffre,
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            ClientService.tokenValidateOrThrows(con, headers);
            
            // delete
            Offre offre = new Offre();
            offre.setIdoffre(idoffre);
            offre.remove(con);

            return CustomResponse.success("success: supprimer offre");
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @GetMapping("/offre")
    public CustomResponse<?> get_all_offre (
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token maitre
            ClientService.tokenValidateOrThrows(con, headers);
            
            ArrayList<Offre> offre = OffreService.getAllOffreWithDetails(con);

            return CustomResponse.success(offre);
        } catch (Exception ex) {
            // ex.printStackTrace();
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }


    @GetMapping("/offre/types")
    public CustomResponse<?> all_types_offre (
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token maitre
            ClientService.tokenValidateOrThrows(con, headers);
            
            ArrayList<TypeOffre> types = OffreService.getAllTypeOffre(con);

            return CustomResponse.success(types);
        } catch (Exception ex) {
            // ex.printStackTrace();
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }
    
    @GetMapping("/offre/type_details")
    public CustomResponse<?> all_type_details (
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            ClientService.tokenValidateOrThrows(con, headers);
            return CustomResponse.success(CustomConfig.OFFRE_TYPE_DETAILS);
        } catch (Exception ex) {
            // ex.printStackTrace();
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @GetMapping("/offre/byidtype/{idtypeoffre}")
    public CustomResponse<?> all_type_details (
        @RequestHeader Map<String, String> headers,
        @PathVariable(required = true) String idtypeoffre
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            ClientService.tokenValidateOrThrows(con, headers);
            ArrayList<Offre> offres  = OffreService.getOffreByType (con, idtypeoffre);
            return CustomResponse.success(offres);
        } catch (Exception ex) {
            // ex.printStackTrace();
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @GetMapping("/offre/special_mobile")
    public CustomResponse<?> all_offre_special (
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            ClientService.tokenValidateOrThrows(con, headers);

            String [] offre_spe_mobile = OffreService.getAllSpecialMobile(con);
            
            return CustomResponse.success(offre_spe_mobile);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }
	
    @GetMapping("/offre/config/prix")
    public CustomResponse<?> offre_config_prix (
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            ClientService.tokenValidateOrThrows(con, headers);
            Map<String, BigDecimal> configs  = PGService.getAllConfig (con);
            return CustomResponse.success(configs);
        } catch (Exception ex) {
            // ex.printStackTrace();
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }
}