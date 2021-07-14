package ws.operateur;

import java.sql.Timestamp;
import java.sql.Connection;
import java.util.Map;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ws.model.BasicStats;
import ws.model.CustomResponse;
import ws.model.DataTransacList;
import ws.services.ClientService;
import ws.services.PGService;
import ws.services.StatsService;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@RestController
public class StatsController {
    @GetMapping("/stats/basics")
    public CustomResponse<?> stats_basics (
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            // fetch all
            BasicStats statistics = StatsService.getBasicStats(con);
            return CustomResponse.success(statistics);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }
    @GetMapping("/stats/transac")
    public CustomResponse<?> stats_transac (
        @RequestHeader Map<String, String> headers
    ) {
        Connection con = null;
        try {
            con = PGService.getConnection();
            // check token
            ClientService.tokenValidateOrThrows(con, headers);
            // fetch all
            Timestamp time = null; // pour l'instant
            DataTransacList stat_datas = StatsService.getBothTranscList(con, time);
            return CustomResponse.success(stat_datas);
        } catch (Exception ex) {
            return CustomResponse.error(ex.getMessage());
        } finally {
            PGService.close(con);
        }
    }
}