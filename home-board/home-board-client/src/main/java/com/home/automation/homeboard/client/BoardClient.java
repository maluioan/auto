package com.home.automation.homeboard.client;

import com.home.automation.homeboard.data.CommandDataList;
import org.springframework.http.ResponseEntity;

public interface BoardClient {
    ResponseEntity<CommandDataList> findActiveCommandsByCount(int commandCount);
}
