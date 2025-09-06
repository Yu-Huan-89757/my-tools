<?xml version="1.0" encoding="UTF-8"?>
<Project>
  <Base>
    <Info name="ProjectName" value="${project_name}" description="${project_description}"/>
  </Base>
  <Parameters>
  <#list params as param>
    <Info name="${param.name}" type="${param.type}" value="" constraints="" optional="${param.optional?c}" description="${param.descriptionZH}">
    </Info>
  </#list>
  </Parameters>

</Project>