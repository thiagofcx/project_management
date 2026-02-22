package com.thiago.projectmanagement.application.usecase;

import com.thiago.projectmanagement.application.dto.PagedResultDTO;
import com.thiago.projectmanagement.application.dto.UserOutputDTO;

public interface ListUsersUseCase {
    PagedResultDTO<UserOutputDTO> execute(int page, int size);
}
