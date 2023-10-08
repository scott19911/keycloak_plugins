package com.example.provider.impl;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class ResourceProvider implements RealmResourceProvider {
    private KeycloakSession session;

    public ResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("/{test}")
    public Response get(@PathParam("test") String refCode) {
        //code
        return Response.status(Response.Status.FOUND).build();
    }

    @Override
    public void close() {
    }
}