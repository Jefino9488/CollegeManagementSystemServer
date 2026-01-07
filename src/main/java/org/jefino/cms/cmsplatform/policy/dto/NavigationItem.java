package org.jefino.cms.cmsplatform.policy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NavigationItem {
    private String code;
    private String label;
    private String icon;
    private String route;
    private List<String> requiredPermissions;
    private List<NavigationItem> children;
}
