package com.ti.test.presentation;

import com.ti.test.model.api.response.BaseApiResponse;
import org.apache.commons.lang3.StringUtils;

class CommonPresentation {

    private CommonPresentation() {}

    public static void showErrorDetails(BaseApiResponse.ErrorTemplate err) {
        String code = err.getCode();
        String description = err.getDescription();
        String params = err.getParams();
        if (code != null || description != null || params != null) {
            System.out.println();
        }
        if (StringUtils.isNotBlank(code)) {
            System.out.println("Codice: " + code);
        }
        if (StringUtils.isNotBlank(description)) {
            System.out.println("Descrizione: " + description);
        }
        if (StringUtils.isNotBlank(params)) {
            System.out.println("Parametri: " + params);
        }
    }
}
