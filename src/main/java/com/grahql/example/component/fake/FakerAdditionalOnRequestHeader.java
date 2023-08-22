package com.grahql.example.component.fake;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@DgsComponent
public class FakerAdditionalOnRequestHeader {

    @DgsQuery
    public String additionalOnRequestHeader(
            @RequestHeader(name = "optionalHeader", required = false) String optionalHeader,
            @RequestHeader(name = "mandatoryHeader")  String mandatoryHeader,
            @RequestParam(name = "optionalParam", required = false)  String optionalParameter,
            @RequestParam(name = "mandatoryParam") String mandatoryParameter
    ) {

        return "Optional Header" + optionalHeader + ", " +
                "Mandatory Header" + mandatoryHeader + ", " +
                "Optional Param" + optionalParameter + ", " +
                "Mandatory Param" + mandatoryParameter + ", ";
    }
}
