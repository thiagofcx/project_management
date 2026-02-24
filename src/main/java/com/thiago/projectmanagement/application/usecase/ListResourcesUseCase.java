package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.dto.ResourceOutputDTO;

public interface ListResourcesUseCase {
    PagedResultDTO<ResourceOutputDTO> execute(int page, int size, String sortBy, String sortOrder, String name, String skills);
}
