{
    "en": {
        "parameters": {
        <#list params as param>
    "${param.name}": {
                "displayName": "${param.displayNameEN}",
                "description": "${param.descriptionEN}"
            }<#if param?index != params?size - 1>, </#if>
        </#list>
        }
    },
    "zh": {
        "parameters": {
        <#list params as param>
            "${param.name}": {
                "displayName": "${param.displayNameZH}",
                "description": "${param.descriptionZH}"
            }<#if param?index != params?size - 1>, </#if>
        </#list>
        }
    }
}