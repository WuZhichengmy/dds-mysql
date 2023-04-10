package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BasicConfig {
    @NotNull
    private String copyright;
    @NotNull
    private String record;
    @NotNull
    private String domainRecord;
    @NotNull
    private String logoUrl;
    @NotNull
    private String titleEn;
    @NotNull
    private String titleCn;
    @NotNull
    private String bannerUrl;
}
