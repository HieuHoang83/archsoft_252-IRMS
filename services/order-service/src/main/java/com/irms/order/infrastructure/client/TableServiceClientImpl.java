package com.irms.order.infrastructure.client;

import com.irms.order.dto.TableResponseDTO;
import com.irms.order.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class TableServiceClientImpl implements TableServiceClient {

    private final RestTemplate restTemplate;
    private final String tableServiceUrl;

    public TableServiceClientImpl(RestTemplate restTemplate, 
                                  @Value("${clients.table-service.base-url}") String tableServiceUrl) {
        this.restTemplate = restTemplate;
        this.tableServiceUrl = tableServiceUrl;
    }

    @Override
    public TableResponseDTO getTable(UUID tableId) {
        try {
            String url = tableServiceUrl + "/api/v1/tables/" + tableId;
            return restTemplate.getForObject(url, TableResponseDTO.class);
        } catch (RestClientException e) {
            throw new ServiceUnavailableException("Table service is currently unavailable", e);
        }
    }
}
