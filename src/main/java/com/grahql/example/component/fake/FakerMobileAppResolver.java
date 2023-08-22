package com.grahql.example.component.fake;

import com.grahql.example.datasource.faker.FakerMobileAppDataSource;
import com.graphql.example.DgsConstants;
import com.graphql.example.types.MobileApp;
import com.graphql.example.types.MobileAppFilter;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.apache.commons.lang3.StringUtils;


import java.time.LocalDate;
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
        System.out.println(filter);
        return filter.map(mobileAppFilter -> FakerMobileAppDataSource.MOBILE_APP_LIST.stream().filter(
                mobileApp -> this.matchFilter(mobileAppFilter, mobileApp)
        ).collect(Collectors.toList())).orElse(FakerMobileAppDataSource.MOBILE_APP_LIST);

    }

    private boolean matchFilter(MobileAppFilter mobileAppFilter, MobileApp mobileApp) {
        boolean isAppMatch = StringUtils.containsIgnoreCase(mobileApp.getName(), mobileAppFilter.getName())
                && StringUtils.containsIgnoreCase(mobileApp.getVersion(), mobileAppFilter.getVersion())
                && mobileApp.getReleaseDate().isAfter(Optional.ofNullable(mobileAppFilter.getReleasedBefore()).orElse(LocalDate.MIN))
                && mobileApp.getDownloaded() >= Optional.of(mobileAppFilter.getMinimunDownloaded()).orElse(0);

        if (isAppMatch) {
            return false;
        }

        if (StringUtils.isNoneBlank(mobileAppFilter.getPlatform())
                && !mobileApp.getPlatform().contains(mobileAppFilter.getPlatform().toLowerCase())) {
            System.out.println(mobileApp.getPlatform());
            System.out.println(mobileAppFilter.getPlatform());
            return false;
        }

        if (mobileAppFilter.getAuthor() != null
                && !StringUtils.containsIgnoreCase(mobileApp.getAuthor().getName(),
                mobileAppFilter.getAuthor().getName())){
            return false;
        }

        if (mobileAppFilter.getCategory() != null && !mobileApp.getCategory().equals(mobileAppFilter.getCategory())) {
            return false;
        }

        return true;
        }
}
