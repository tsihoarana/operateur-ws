package ws.operateur;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ws.model.Client;
import ws.model.CustomConfig;
import ws.model.CustomResponse;
import ws.model.PageClient;
import ws.model.StatusClient;
import ws.model.Token;
import ws.services.ClientService;
import ws.services.PGService;
import ws.template_json.ConnexionJSON;
import ws.template_json.InscriptionJSON;
import ws.tools.Tools;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@RestController
public class ClientController {

    @PostMapping("/client/connexion")
    public CustomResponse<?> client_connexion (@RequestBody ConnexionJSON input) {
        Connection con = null;
        Token token = null;
        try {
            input.validateOrThrows(); // valide l'input
            con = PGService.getConnection();
            con.setAutoCommit(false);
            token = ClientService.checkUserPassAndFetchToken(con, input);
            con.commit();
            return CustomResponse.success(token);
        } catch (Exception ex) {
            PGService.rollback(con);

            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @PostMapping("/client/inscription")
    public CustomResponse<?> client_inscription (@RequestBody InscriptionJSON input) {
        Connection con = null;
        try {
            input.validateOrThrows(); // valide l'input
            con = PGService.getConnection();
            ClientService.addClientUsing (input, CustomConfig.CODE_BASIC_USER); // insert
            return CustomResponse.success("succes inscription pour " + input.getNumero());
        } catch (Exception ex) {
            PGService.rollback(con);

            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @PostMapping("/client/admin/connexion")
    public CustomResponse<?> client_admin_connexion (@RequestBody ConnexionJSON input) {
        Connection con = null;
        Token token = null;
        try {
            input.validateOrThrows(); // valide l'input
            con = PGService.getConnection();
            con.setAutoCommit(false);
            ClientService.checkIfAdmin (con, input.getNumero());
            token = ClientService.checkUserPassAndFetchToken(con, input);
            con.commit();
            return CustomResponse.success(token);
        } catch (Exception ex) {
            PGService.rollback(con);

            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @PostMapping("/client/admin/inscription")
    public CustomResponse<?> client_admin_inscription (@RequestBody InscriptionJSON input) {
        Connection con = null;
        try {
            input.validateOrThrows(); // valide l'input
            con = PGService.getConnection();
            ClientService.addClientUsing (input, CustomConfig.CODE_ADMIN); // insert
            return CustomResponse.success("succes inscription admin pour " + input.getNumero());
        } catch (Exception ex) {
            // PGService.rollback(con);
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @GetMapping("/client")
    public CustomResponse<?> client_fetchAll (
            @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            // fetch all
            ArrayList<Client> all = ClientService.fetchAll(con);
            return CustomResponse.success(all);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }

    @GetMapping("/client/status/{idclient}")
    public CustomResponse<?> status_client (
        @PathVariable(required = true) String idclient,
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            // fetch all
            StatusClient status = ClientService.getStatusClientComplet(con, idclient);
            return CustomResponse.success(status);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
	}
    // /client/search?keyword=&page=1
    @GetMapping("/client/search")
    public CustomResponse<?> search_client (
        @RequestHeader Map<String, String> headers,
        @RequestParam(value = "keyword", defaultValue = "") String keyword,
        @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            Tools.negatifOrZeroChecker(page, "page");

            PageClient p = ClientService.getPageSearch(con, keyword, page - 1);

            return CustomResponse.success(p);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
	}
}