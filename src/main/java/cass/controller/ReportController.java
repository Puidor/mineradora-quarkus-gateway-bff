package cass.controller;

import cass.service.ReportService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Date;

@ApplicationScoped
@Path("/api/opportunity")
public class ReportController {

    @Inject
    ReportService reportService;

    @GET
    @Path("/report")
    @RolesAllowed({"user","manager"})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateReport(){

        try {
            return Response.ok(reportService.generateCSVOpportunityReport(),
                            MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition",
                            "attachment; filename = "+ new Date() +"--oportunidades-venda.csv").
                    build();
        } catch (ServerErrorException errorException) {
            return Response.serverError().build();
        }

    }

    @GET
    @Path("/data")
    @RolesAllowed({"user","manager"})
    public Response generateOpportunitiesData(){

        try {
            return Response.ok(reportService.getOpportunitiesData(),
                    MediaType.APPLICATION_JSON).build();
        } catch (ServerErrorException errorException) {
            return Response.serverError().build();
        }

    }

}
