package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.AllocationOutputDTO;
import com.thiago.projectmanagement.application.dto.PagedResultDTO;

public interface ListAllocationsUseCase {
    PagedResultDTO<AllocationOutputDTO> execute(int page, int size, String sortBy, String sortOrder, String resourceName, String projectName);
}
