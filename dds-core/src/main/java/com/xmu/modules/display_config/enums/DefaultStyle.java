package com.xmu.modules.display_config.enums;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
public enum DefaultStyle {

    CAROUSEL("{\"style\": {\"width\": \"316px\", \"padding\": \"0 5px\", \"marginRight\": \"0\"}, \"autoplay\": \"4000\"}",0),
    CATEGORY("{\"color\": [\"#C23531\", \"#215643\", \"#427DCC\"], \"xAxis\": [{\"name\": \"时间(年)\", \"type\": \"category\"}], \"yAxis\": [{\"name\": \"数量 (篇)\", \"type\": \"value\"}], \"series\": [{\"name\": \"资源数量\", \"type\": \"bar\", \"barWidth\": \"75%\", \"showBackground\": true}]}",1),
    LEADERBOARD("{\"clamp\": 1, \"column\": 2, \"indexStyle\": \"number\"}",2),
    PANEL("{\"icon\": [\"international\", \"education\", \"tree\"], \"color\": [\"#40c9c6\", \"#f4516c\", \"#215643\"]}",3),
    TAGS_CLOUD("{\"color\": [\"#427dc9\", \"#a69fd5\", \"#4152b9\", \"#34418d\", \"#e59c6f\", \"#629667\", \"#85b7be\", \"#8dc9b0\", \"#f29a9e\"], \"speed\": 1000, \"width\": 480, \"height\": 410, \"randomColor\": true}",4),
    ARTICLE_LIST("{\"icon\": [\"md-globe\", \"md-easel\", \"md-paper\"]}",5),
    BANNER(null,6),
    LOGO(null,7),
    COPYRIGHT(null,8);
    private String config;
    private Integer type;
    DefaultStyle(String config, Integer type){
        this.config=config;
        this.type=type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
