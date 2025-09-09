package cass.service;

import cass.dto.OpportunityDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.ByteArrayInputStream;
import java.util.List;

@ApplicationScoped
public interface ReportService {

        ByteArrayInputStream generateCSVOpportunityReport();

        List<OpportunityDTO> getOpportunitiesData();
}
