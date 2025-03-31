package com.kondra.kos.codex;

import com.tccc.kos.commons.core.service.config.ConfigBean;
import com.tccc.kos.commons.core.service.config.annotations.ConfigDesc;
import com.tccc.kos.commons.core.service.config.annotations.ConfigFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodexServiceConfig extends ConfigBean {
    @ConfigDesc(value = "volume stored in ml", format = ConfigFormat.ML)
    private int volume = 0;

    @ConfigDesc(value = "Colors")
    private Color colors = Color.DEFAULT;

    @ConfigDesc(value = "unitSystemRangeInterval", options = "{type:'unitSystemRangeInterval', unitSystems: {si: {start:0, end:50, interval:7.5, decimals:2}, us: {start:0, end:100, interval:13.5, decimals:1}}}")
    private double unitSystemRangeInterval = 0;

    enum Color {
        DEFAULT("#FF0000"),
        RED("#FF0000"),
        BLUE("#0000FF"),
        GREEN("#00FF00");

        final String hexCode;

        Color(String hexCode) {
            this.hexCode = hexCode;
        }
    }
}
