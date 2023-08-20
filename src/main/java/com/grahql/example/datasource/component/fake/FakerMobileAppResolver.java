package com.grahql.example.datasource.component.fake;

import com.grahql.example.datasource.faker.FakerMobileAppDataSource;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.MobileApp;
import com.graphql.example.types.MobileAppFilter;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.apache.commons.lang3.StringUtils;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DgsComponent
public class FakerMobileAppResolver {

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.MobileApps
    )
    public List<MobileApp> getApps(@InputArgument(name = "mobileAppFilter") Optional<MobileAppFilter> filter) {
        return filter.map(mobileAppFilter -> FakerMobileAppDataSource.MOBILE_APP_LIST.stream().filter(
                mobileApp -> this.matchFilter(mobileAppFilter, mobileApp)
        ).collect(Collectors.toList())).orElse(FakerMobileAppDataSource.MOBILE_APP_LIST);

    }

    private boolean matchFilter(MobileAppFilter mobileAppFilter, MobileApp mobileApp) {
        boolean isAppMatch = StringUtils.containsIgnoreCase(mobileApp.getName(),
                StringUtils.defaultIfBlank(mobileAppFilter.getName(), StringUtils.EMPTY))
                && StringUtils.containsIgnoreCase(mobileApp.getVersion(),
                StringUtils.defaultIfBlank(mobileAppFilter.getVersion(), StringUtils.EMPTY)
                );
        if (isAppMatch) {
            return false;
        }

        if (StringUtils.isNoneBlank(mobileAppFilter.getPlatform())
                && !mobileApp.getPlatform().contains(mobileAppFilter.getPlatform().toLowerCase())) {
            return false;
        }

        if (mobileApp.getAuthor() != null
                && !StringUtils.containsIgnoreCase(mobileApp.getAuthor().getName(),
                StringUtils.defaultIfBlank(mobileAppFilter.getAuthor().getName(), StringUtils.EMPTY))){
            return false;
        }

        return true;
        }
}
