package com.movie.api.service;

import com.movie.api.model.dto.CxLoginDto;

public interface CxAdminService {

    String login(CxLoginDto cxLoginDto) throws Exception;

}
