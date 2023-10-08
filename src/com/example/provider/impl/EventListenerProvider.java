package com.example.provider.impl;

import org.keycloak.events.Event;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import java.util.Map;



public class EventListenerProvider implements org.keycloak.events.EventListenerProvider {
    private KeycloakSession keycloakSession;
    public EventListenerProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    @Override
    public void onEvent(Event event) {

        RealmModel realmModel = keycloakSession.getContext().getRealm();
        if(event.getUserId() == null) return;
        UserModel userModel = keycloakSession.users().getUserById(realmModel, event.getUserId());
        //example delete all user session except current when event type LOGIN
        if (event.getType().equals(EventType.LOGIN)) {
            keycloakSession.sessions().getUserSessionsStream(realmModel, userModel).forEach(userSession -> {
                if (!userSession.getId().equals(event.getSessionId())) {
                    keycloakSession.sessions().removeUserSession(realmModel, userSession);
                }
            });
            keycloakSession.sessions().getOfflineUserSessionsStream(realmModel, userModel).forEach(userSession -> {
                if (!userSession.getId().equals(event.getSessionId())) {
                    keycloakSession.sessions().removeOfflineUserSession(realmModel, userSession);
                }
            });
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
    }

    @Override
    public void close() {
    }

    private String toString(Event event) {
        StringBuilder sb = new StringBuilder();
        sb.append("type=");
        sb.append(event.getType());
        sb.append(", realmId=");
        sb.append(event.getRealmId());
        sb.append(", clientId=");
        sb.append(event.getClientId());
        sb.append(", userId=");
        sb.append(event.getUserId());
        sb.append(", ipAddress=");
        sb.append(event.getIpAddress());

        if (event.getError() != null) {
            sb.append(", error=");
            sb.append(event.getError());
        }
        if (event.getDetails() != null) {
            for (Map.Entry<String, String> e : event.getDetails().entrySet()) {
                sb.append(", ");
                sb.append(e.getKey());
                if (e.getValue() == null || e.getValue().indexOf(' ') == -1) {
                    sb.append("=");
                    sb.append(e.getValue());
                } else {
                    sb.append("='");
                    sb.append(e.getValue());
                    sb.append("'");
                }
            }
        }
        return sb.toString();
    }


    private String toString(AdminEvent adminEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("operationType=");
        sb.append(adminEvent.getOperationType());
        sb.append(", realmId=");
        sb.append(adminEvent.getAuthDetails().getRealmId());
        sb.append(", clientId=");
        sb.append(adminEvent.getAuthDetails().getClientId());
        sb.append(", userId=");
        sb.append(adminEvent.getAuthDetails().getUserId());
        sb.append(", ipAddress=");
        sb.append(adminEvent.getAuthDetails().getIpAddress());
        sb.append(", resourcePath=");
        sb.append(adminEvent.getResourcePath());

        if (adminEvent.getError() != null) {
            sb.append(", error=");
            sb.append(adminEvent.getError());
        }
        return sb.toString();
    }
}