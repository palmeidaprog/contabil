package com.react.contabil.conta;

import com.mysql.cj.util.TestUtils;
import com.react.contabil.datalayer.dataobject.UsuarioDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/conta")
public class ContaService {

    private static final Logger LOGGER = LoggerFactory
    			.getLogger(ContaService.class);

    private ContaServiceHandler handler = new ContaServiceHandler();

    @GET
    @Path("/test")
    public Response test() {
        final UsuarioDO usuarioDO = new UsuarioDO();
        usuarioDO.setLogin("teste");
        usuarioDO.setNome("Tester");
        usuarioDO.setSobrenome("Test");
        this.handler.inserir(usuarioDO);

        return Response.ok().build();
    }

}
