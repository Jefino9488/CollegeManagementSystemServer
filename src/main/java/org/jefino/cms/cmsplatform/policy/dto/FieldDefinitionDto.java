package org.jefino.cms.cmsplatform.policy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FieldDefinitionDto {
    private UUID id;
    private String fieldCode;
    private String fieldType;
    private String label;
    private int displayOrder;
    private boolean required;
    private Map<String, Object> config;
}
