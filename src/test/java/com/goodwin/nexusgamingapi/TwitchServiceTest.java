package com.goodwin.nexusgamingapi;

import com.goodwin.nexusgamingapi.service.TwitchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Tells JUNIT to use Mockito
public class TwitchServiceTest {

    @Mock // Create a fake RestTemplate
    private RestTemplate restTemplate;

    @InjectMocks // plugs the fake RestTemplate into your real service
    private TwitchService twitchService;

    @Test
    @DisplayName("Should return cached token on second call")
    void getAccessToken_ShouldReturnCachedToken(){

        // ARRANGE (Set up the "Fake" behavior)
        // Tell the fake RestTemplate exactly what to return the first time
        TwitchTokenResponse mockResponse = new TwitchTokenResponse();
        mockResponse.setAccessToken("fake-token-123");
        mockResponse.setExpiresIn(3600); // 1 hour

        doReturn(mockResponse).when(restTemplate).postForObject(
                nullable(String.class), // Matches the null URL in the test
                any(),                  // Matches any request body
                any()                   // Matches any response class type
        );

        // ACT
        twitchService.getAccessToken(); // calls for API
        TwitchTokenResponse secondToken = twitchService.getAccessToken();

        // ASSERT & VERIFY
        assertNotNull(secondToken, "The service returned null! The mock didn't trigger.");
        assertEquals("fake-token-123", secondToken.getAccessToken());

        // Checks to make sure postForObject was only executed once
        verify(restTemplate, times(1)).postForObject(nullable(String.class), any(), any());
    }
}
